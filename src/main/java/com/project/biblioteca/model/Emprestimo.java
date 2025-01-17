package com.project.biblioteca.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
public class Emprestimo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@Size(min = 4, max = 4, message = "ISBN deve ter 4 caracteres")
	private String isbn;
	@NotNull
	@Size(min = 13, max = 13, message = "RA deve ter 13 caracteres")
	private String RA;
	private String dataEmprestimo;
	private String dataDevolucao;
	private String dataDevolucaoPrevista;

	public Emprestimo(String isbn, String ra) {
		this.isbn = isbn;
		this.RA = ra;
		DateTime dataAtual = new DateTime();
		setDataEmprestimo(dataAtual);
	}

	public Emprestimo() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getRA() {
		return RA;
	}

	public void setRA(String ra) {
		this.RA = ra;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(DateTime dataAtual) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		this.dataEmprestimo = dataAtual.toString(fmt);
		setDataDevolucaoPrevista();
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(DateTime dataDevolucao) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		this.dataDevolucao = dataDevolucao.toString(fmt);
	}

	public String getDataDevolucaoPrevista() {
		return dataDevolucaoPrevista;
	}

	private void setDataDevolucaoPrevista() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		DateTime data = fmt.parseDateTime(getDataEmprestimo());
		String dataDev = data.plusDays(8).toString(fmt);
		if(ehDomingo(dataDev)) {
			dataDev = data.plusDays(9).toString(fmt);
		}
		this.dataDevolucaoPrevista = dataDev;
	}

	public boolean validaData(String data) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		df.setLenient(false); //
		try {
			df.parse(data); // data válida
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	public boolean ehDomingo(String data) {
		boolean isValida = false;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
		if (validaData(data) == true) {
			DateTime umaData = fmt.parseDateTime(data);
			if (umaData.dayOfWeek().getAsText().equals("Domingo")) {
				isValida = true;
			}
		}
		return isValida;
	}

	public Integer verificaAtraso() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		DateTime dataDevolucao = fmt.parseDateTime(getDataDevolucao());
		DateTime dataDevolucaoPrevista = fmt.parseDateTime(getDataDevolucaoPrevista());
		int dias = Days.daysBetween(dataDevolucaoPrevista, dataDevolucao).getDays();
		return dias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emprestimo other = (Emprestimo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}