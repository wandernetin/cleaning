package controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import entities.InvoiceReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.PublicHolidayService;

@ManagedBean(name = "invoiceController")
@ViewScoped
public class InvoiceController implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<InvoiceReport> invoiceList;

	private Date dateBegin;

	private Date dateEnd;

	private Double currentValue;

	private Integer invoiceNumber;

	@EJB
	private PublicHolidayService pubHolService;

	@SuppressWarnings("unchecked")
	public void click() {
		invoiceList = new ArrayList<InvoiceReport>();

		Date currentDate = dateBegin;

		int iteration = 1;

		while (currentDate.before(dateEnd)) {
			InvoiceReport invoice = new InvoiceReport();

			if (!pubHolService.isThisDateAHoliday(currentDate)) {
				invoice.setDate(new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ENGLISH).format(currentDate));
				invoice.setTotalDate(currentValue * iteration);
				invoiceList.add(invoice);
				iteration++;
			}

			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			if (c.get(Calendar.DAY_OF_WEEK) == 6) {
				c.add(Calendar.DATE, 3);
			} else {
				c.add(Calendar.DATE, 1);
			}

			currentDate = c.getTime();

		}

		@SuppressWarnings("rawtypes")
		Map parametersMap = new HashMap();
		parametersMap.put("date", new Date());
		parametersMap.put("invoiceNumber", getInvoiceNumber());
		parametersMap.put("total", invoiceList.size() * currentValue);

		try {
			//String jrxmlFileName = "report/invoice1.jrxml";
			//String jasperFileName = "report/invoice1.jasper";

			URL arquivo = getClass().getResource("/report/invoice1.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivo);

		    JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parametersMap,
					new JRBeanCollectionDataSource(invoiceList));
		    
		    
		    HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();  
		    httpServletResponse.addHeader("Content-disposition", "attachment; filename=invoice.pdf");  

		    FacesContext.getCurrentInstance().responseComplete();


		    ServletOutputStream servletOutputStream;
			try {
				servletOutputStream = httpServletResponse.getOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
			    System.out.println("All done the report is done");
			    servletOutputStream.flush();
			    servletOutputStream.close(); 
			    FacesContext.getCurrentInstance().responseComplete();  
			    
			} catch (IOException e) {
				e.printStackTrace();
			}  
		    
		    

		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public List<InvoiceReport> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<InvoiceReport> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
}
