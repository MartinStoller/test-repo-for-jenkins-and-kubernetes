package de.example.haegertime.invoice;

import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/invoices")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class InvoiceController {

    private final InvoiceService invoiceService;


    @PostMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response, @RequestParam Long customerId,
                              @RequestParam Long projectId) throws IOException{
        invoiceService.exportToExcel(response, customerId, projectId);
    }


    @PostMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response,@RequestParam Long customerId,
                            @RequestParam Long projectId) throws DocumentException, IOException {
        invoiceService.exportToPdf(response, customerId, projectId);
    }


    @PostMapping(value = "/export/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceXMLExporter exportToXML(HttpServletResponse response,@RequestParam Long customerId,@RequestParam Long projectId)
            throws IOException {
        return invoiceService.exportToXML(response, customerId, projectId);
    }


}
