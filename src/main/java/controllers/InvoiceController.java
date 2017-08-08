package controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import entities.InvoiceReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name = "invoiceController")
@ViewScoped
public class InvoiceController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<InvoiceReport> invoiceList;

	private Date dateBegin;

	private Date dateEnd;

	private Double currentValue;
	
	private Integer invoiceNumber;

	@SuppressWarnings("unchecked")
	public void click() {
		invoiceList = new ArrayList<InvoiceReport>();

		Date newDate = dateBegin;

		int iteration = 1;

		while (newDate.before(dateEnd)) {
			InvoiceReport invoice = new InvoiceReport();

			invoice.setDate(new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ENGLISH).format(newDate));
			invoice.setTotalDate(currentValue * iteration);
			invoiceList.add(invoice);

			Calendar c = Calendar.getInstance();
			c.setTime(newDate);
			if (c.get(Calendar.DAY_OF_WEEK) == 6) {
				c.add(Calendar.DATE, 3);
			} else {
				c.add(Calendar.DATE, 1);
			}

			newDate = c.getTime();
			iteration++;

		}

		@SuppressWarnings("rawtypes")
		Map parametersMap = new HashMap();
		parametersMap.put("date", new Date());
		parametersMap.put("invoiceNumber", getInvoiceNumber());
		parametersMap.put("total", 200.00);

		try {
			String jrxmlFileName = "C:/AmbienteJEE/Relatorios/invoice1.jrxml";
			String jasperFileName = "C:/AmbienteJEE/Relatorios/invoice1.jasper";

			JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, parametersMap,
					new JRBeanCollectionDataSource(invoiceList));

			JasperExportManager.exportReportToPdfFile(jprint, "C:/Users/wande/Downloads/Invoice.pdf");

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