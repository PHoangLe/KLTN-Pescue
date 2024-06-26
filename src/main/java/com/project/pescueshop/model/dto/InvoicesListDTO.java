package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "invoice", pluralNoun = "invoiceList")
public class InvoicesListDTO {
    private List<Invoice> invoices;
    private List<LiveInvoice> liveInvoices;
}
