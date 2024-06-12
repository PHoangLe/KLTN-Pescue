package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("select i from Invoice i where i.userId = ?1 ")
    List<Invoice> findAllInvoiceByUserId(String userId);

    @Query("select i from Invoice i where i.createdDate >= ?1 and i.createdDate <= ?2")
    List<Invoice> findInvoiceByDateRange(Date fromDate, Date toDate);

    @Query(
            "select i, u.userFirstName, u.userLastName " +
            "from Invoice i " +
            "join User u on i.userId = u.userId "
    )
    List<Object[]> findAllInvoice();
}
