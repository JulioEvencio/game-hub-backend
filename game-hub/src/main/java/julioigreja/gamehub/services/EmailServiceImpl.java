package julioigreja.gamehub.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    private final String frontEndURL;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender javaMailSender, @Value("${api.url.front-end}") String frontEndURL) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;

        this.frontEndURL = frontEndURL;
    }

    @Async
    @Override
    public void sendEmailRegister(String username, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Welcome to Game Hub");

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("frontEndURL", frontEndURL);

            String htmlContent = templateEngine.process("welcome-email", context);
            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            // Code
        }
    }

}
