package easycommerce.easycommerce.Correo;

import java.util.List;
import java.util.Optional;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import easycommerce.easycommerce.Estados.CambioEstado.Model.CambioEstado;
import easycommerce.easycommerce.Excepciones.NoSuchElementException;
import easycommerce.easycommerce.Excepciones.UserAlreadyVerifiedException;
import easycommerce.easycommerce.Pedido.Model.Pedido;
import easycommerce.easycommerce.Pedido.Repository.PedidoRepository;
import easycommerce.easycommerce.Usuario.Model.Usuario;
import easycommerce.easycommerce.Usuario.Repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public CorreoService(JavaMailSender javaMailSender, TemplateEngine templateEngine,
            PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void enviarEmailEstadoPedido(Long idPedido) throws MessagingException, NoSuchElementException{
        
        Optional<Pedido> pedidoBd = pedidoRepository.findById(idPedido);
        if(pedidoBd.isPresent()){
            Pedido pedido = pedidoBd.get();
            String email = pedido.getCliente().getEmail();
            String asunto = "Pedido N° " + pedido.getId() + " " + "GNR Computación";

            List<CambioEstado> ce = pedido.getCambiosEstado();
            CambioEstado cambioEstadoActual = ce.stream()
            .filter(CambioEstado::esActual)
            .findFirst()
            .orElse(null);

            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(email);
            helper.setSubject(asunto);
        
            Context contexto = new Context();
            //seleccionar las variables con contexto.setVariable(nombrevariable,datos)
            contexto.setVariable("pedido", pedido);
            contexto.setVariable("cambioestado", cambioEstadoActual);
            contexto.setVariable("condicionIvaCliente", pedido.getCliente().getCondicionIva().name());

            String contenidoHTML = templateEngine.process("templateMail", contexto);
            helper.setText(contenidoHTML,true);

            javaMailSender.send(mensaje);
        }
        else{
            throw new NoSuchElementException("No se encuentra el pedido con id: " + idPedido);
        }
    }

    public void enviarEmailEstadoPedidoAdministracion(Long idPedido) throws MessagingException, NoSuchElementException{

        Optional<Pedido> pedidoBd = pedidoRepository.findById(idPedido);
        if(pedidoBd.isPresent()){
            Pedido pedido = pedidoBd.get();
            String email = "administracion@gnrcomputacion.com.ar";
            String asunto = "Pedido N° " + pedido.getId() + " " + "GNR Computación";

            List<CambioEstado> ce = pedido.getCambiosEstado();
            CambioEstado cambioEstadoActual = ce.stream()
                    .filter(CambioEstado::esActual)
                    .findFirst()
                    .orElse(null);

            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(email);
            helper.setSubject(asunto);

            Context contexto = new Context();
            //seleccionar las variables con contexto.setVariable(nombrevariable,datos)
            contexto.setVariable("pedido", pedido);
            contexto.setVariable("cambioestado", cambioEstadoActual);

            String contenidoHTML = templateEngine.process("templateMail", contexto);
            helper.setText(contenidoHTML,true);

            javaMailSender.send(mensaje);
        }
        else{
            throw new NoSuchElementException("No se encuentra el pedido con id: " + idPedido);
        }
    }

    public void enviarMailCodigoVerficacion(String username) throws NoSuchElementException, MessagingException, UserAlreadyVerifiedException{
        Optional<Usuario> usuarioBd = usuarioRepository.findByUsername(username);
        if(usuarioBd.isPresent()){
            Usuario usuario = usuarioBd.get();
            if(!usuario.isValidado()){
                String email = usuario.getUsername();
                String asunto = "Verificacion de correo electronico para tienda GNR Computacion";

                MimeMessage mensaje = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

                helper.setTo(email);
                helper.setSubject(asunto);
        
                Context contexto = new Context();
                //seleccionar las variables con contexto.setVariable(nombrevariable,datos)
                contexto.setVariable("username", usuario.getUsername());
                contexto.setVariable("codigo", usuario.getCodigoVerificacion().getCodigo());

                String contenidoHTML = templateEngine.process("templateCorreoVerificacion", contexto);
                helper.setText(contenidoHTML,true);

                javaMailSender.send(mensaje);
            }
            else{
                throw new UserAlreadyVerifiedException("El usuario ya se encuentra validado");
            }
            
        }
        else{
            throw new NoSuchElementException("No se encuentra un usuario con username " + username);
        }
    }

    public void enviarMailRegistroCorrecto(String emailCliente) throws NoSuchElementException, MessagingException, UserAlreadyVerifiedException{
        String email = "administracion@gnrcomputacion.com.ar";
        String asunto = "Cliente Registrado Exitosamente";

        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(email);
        helper.setSubject(asunto);
        
        Context contexto = new Context();
        //seleccionar las variables con contexto.setVariable(nombrevariable,datos)
        contexto.setVariable("email", emailCliente);

        String contenidoHTML = templateEngine.process("templateCorreoRegistroExitoso", contexto);
        helper.setText(contenidoHTML,true);

        javaMailSender.send(mensaje);
    }
}
