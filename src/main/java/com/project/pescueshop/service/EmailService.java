package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.InvoiceItem;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.model.entity.Variety;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.util.constant.EnumResponseCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${MAIL_USERNAME}")
    private String sender;

    public void sendMail(String receiverEmail, String emailBody, String subject) throws FriendlyException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(sender);
            helper.setTo(receiverEmail);
            helper.setSubject(subject);

            boolean html = true;

            helper.setText(
                    "<body style='width: 100%;'>" +
                            "  <div class='header'>" +
                            "          <img src='https://firebasestorage.googleapis.com/v0/b/advance-totem-350103.appspot.com/o/Logo%2FMail_Banner.png?alt=media&token=657ab2e2-addb-4545-9476-1b4e730c79ff' style='border:0; display:block; outline:none; text-decoration:none; height:auto; min-width:60%; width:60%; max-width:60%; font-size:13px; margin:auto;'>" +
                            "      </div>" +
                            "      <div style='padding: 20px; width: 100%;'>" +
                            "         <div style='margin: auto; padding: 20px; min-width:60%; width:60%; max-width:60%; font-size: 16px;'>" +
                            "             <p>" + emailBody + "</p>" +
                            "         </div>" +
                            "      </div>" +
                            "      <div style='width: 100%; padding: 20px;' class='footer'>" +
                            "         <div style='margin: auto; padding: 20px; min-width:60%; width:60%; max-width:60%; font-weight: 700; font-size: 16px; color: #7A3D26; border-top: 1px solid black;'>" +
                            "             <p>Address: Ho Chi Minh city, Viet Nam</p>" +
                            "             <p>Phone Number: +84 902-309-287</p>" +
                            "         </div>" +
                            "      </div>" +
                            "  </body>", html);

            javaMailSender.send(message);

            log.trace("Mail has been sent to: " + receiverEmail);

        }
        catch (Exception e){
            throw new FriendlyException(EnumResponseCode.MAIL_SENT_FAIL);
        }
    }

    public void sendInvoiceEmail(List<InvoiceItemDTO> invoiceItems, Invoice invoice) throws FriendlyException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(sender);
            helper.setTo(invoice.getUserEmail());
            helper.setSubject("Invoice Receipt");

            boolean html = true;

            String header = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "    <title>Your Email Title</title>" +
                    "    <style>" +
                    "        td {" +
                    "            height: 30px;" +
                    "        }" +
                    "    </style>" +
                    "</head>" +
                    "<body style=\"width: 100%;\">" +
                    "    <div class=\"header\">" +
                    "        <img src=\"https://firebasestorage.googleapis.com/v0/b/advance-totem-350103.appspot.com/o/Logo%2FMail_Banner.png?alt=media&token=657ab2e2-addb-4545-9476-1b4e730c79ff\"" +
                    "            style=\"border: 0; display: block; outline: none; text-decoration: none; height: auto; min-width: 60%; width: 60%; max-width: 60%; font-size: 13px; margin: auto;\">" +
                    "    </div>";

            
            String shippingInfo = "    <div style=\"padding: 20px; width: 100%;\">" +
                    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"" +
                    "            style=\"border: 0;  outline: none; text-decoration: none; height: auto; min-width: 58%; width: 58%; max-width: 60%; font-size: 16px; margin: auto;\">" +
                    "            <tr style=\"margin: 8px;\">" +
                    "                <td align=\"left\">" +
                    "                    Name:" +
                    "                </td>" +
                    "                <td align=\"left\">" +
                                    invoice.getRecipientName() +
                    "                </td>" +
                    "            </tr>" +
                    "            <tr style=\"margin: 8px;\">" +
                    "                <td align=\"left\">" +
                    "                    Phone number:" +
                    "                </td>" +
                    "                <td align=\"left\">" +
                                         invoice.getPhoneNumber() +
                    "                </td>" +
                    "            </tr>" +
                    "            <tr style=\"margin: 8px;\">" +
                    "                <td align=\"left\">" +
                    "                    Address:" +
                    "                </td>" +
                    "                <td align=\"left\">" +
                                        invoice.getStreetName() + ", " + invoice.getWardName() + ", " + invoice.getDistrictName() + ", " + invoice.getCityName() +
                    "                </td>" +
                    "            </tr>" +
                    "            <tr style=\"margin: 8px;\">" +
                    "                <td align=\"left\">" +
                    "                    Email:" +
                    "                </td>" +
                    "                <td align=\"left\">" +
                                        invoice.getUserEmail() +
                    "                </td>" +
                    "            </tr>" +
                    "            <tr>" +
                    "                <td colspan=\"2\">" +
                    "                    <div style=\"border-bottom: solid 1px #7A3D26; margin-top: 24px;\"></div>" +
                    "                </td>" +
                    "            </tr>" +
                    "            <tr>";

            StringBuilder body = new StringBuilder();

            for (InvoiceItemDTO item : invoiceItems){
                body
                        .append("<td align=\"left\" style=\"width: 120px;\">")
                        .append("<img width=\"80px\" height=\"80px\"")
                        .append("src=\"").append(item.getImage()).append("\">")
                        .append("</td>")
                        .append("<td align=\"left\">")
                        .append("<p>").append(item.getName()).append("</p>")
                        .append("<p>").append(item.getUnitPrice()).append(" VND </p>")
                        .append("<p>Amount: ").append(item.getQuantity()).append("</p>")
                        .append("</td>")
                        .append("<tr>")
                        .append("<td colspan=\"2\">")
                        .append("<div style=\"border-bottom: solid 1px #7A3D26; margin-top: 24px;\"></div>")
                        .append("</td>")
                        .append("</tr>");
            }

            String footer = "<tr>" +
                    "                        <td colspan=\"2\" align=\"right\">Total : " + invoice.getTotalPrice() + " VND</td>" +
                    "                    </tr>" +
                    "<tr>" +
                    "                        <td colspan=\"2\" align=\"right\">Discount : " + invoice.getDiscountPrice() + " VND</td>" +
                    "                    </tr>" +
                    "<tr>" +
                    "                        <td colspan=\"2\" align=\"right\">Final : " + invoice.getFinalPrice() + " VND</td>" +
                    "                    </tr>" +
                    "                </tr>" +
                    "" +
                    "            </table>" +
                    "        </table>" +
                    "    </div>" +
                    "    <div style=\"width: 100%; padding: 20px;\" class=\"footer\">" +
                    "        <div" +
                    "            style=\"margin: auto; padding: 20px; min-width: 60%; width: 60%; max-width: 60%; font-weight: 700; font-size: 16px; color: #7A3D26; border-top: 1px solid black;\">" +
                    "            <p>Địa chỉ: Ho Chi Minh city, Viet Nam</p>" +
                    "            <p>Số điện thoại: +84 902-309-287</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "" +
                    "</html>";

            String finalBody = header + shippingInfo + body + footer;

            helper.setText(finalBody, html);

            javaMailSender.send(message);

            log.trace("Mail has been sent to: " + invoice.getUserEmail());

        }
        catch (Exception e){
            log.trace(e.getMessage());
            throw new FriendlyException(EnumResponseCode.MAIL_SENT_FAIL);
        }
    }

}
