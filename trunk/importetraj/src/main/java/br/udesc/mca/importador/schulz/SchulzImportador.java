package br.udesc.mca.importador.schulz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.usuario.Usuario;

public class SchulzImportador {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/schulz.properties");
		props.load(file);
		return props;
	}

	public static void main(String[] args) throws IOException {
		Properties prop = SchulzImportador.getProp();
		File diretorio = new File(prop.getProperty("prop.base.caminho"));
		String[] ext = { prop.getProperty("prop.base.extesao") };

		Iterator<File> arquivos = null;
		SimpleDateFormat formataDataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd");
		Usuario usuario = null;
		Trajetoria trajetoria = null;

		List<Ponto> listaPonto = null;

		String base = prop.getProperty("prop.base.nome");
		String arquivoTrajetoria = null;
		String usuarioTrajetoria = null;
		String latitude = null;
		String longitude = null;
		String dataTrajetoria = null;
		String dataPonto = null;
		String horaPonto = null;
		String acuracia = null;

		String arquivoTrajetoriaAux = null;

		int recursivo = Integer.parseInt(prop.getProperty("prop.base.recursivo"));
		int pontosPorSegmento = Integer.parseInt(prop.getProperty("prop.base.ponto.segmento"));
		int contador = 0;
		int contaLinha = 0;

		// iniciando leitura dos arquivos da pasta
		if (recursivo == 0) {
			arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		} else {
			arquivos = FileUtils.iterateFiles(diretorio, ext, true);
		}

		while (arquivos.hasNext()) {
			trajetoria = new Trajetoria();
			listaPonto = new ArrayList<Ponto>();
			File arquivo = arquivos.next();
			FileReader conteudoArquivo = new FileReader(arquivo);
			BufferedReader bufferConteudo = new BufferedReader(conteudoArquivo);
			String linha = null;

			contador++;
			System.out.println("Arquivo: " + contador + "-" + arquivo.getName().trim().toLowerCase());

			while ((linha = bufferConteudo.readLine()) != null) {
				// pular linhas em branco
				if (linha.trim().length() == 0) {
					continue;
				}

				// a primeira linha
				if (contaLinha == 0) {
					contaLinha++;
					continue;
				}

				// usuario;base;arquivo;datat;lat;lon;datap;horap;acuracia
				StringTokenizer tokenizer = new StringTokenizer(linha, prop.getProperty("prop.base.separador.dados"));

				usuarioTrajetoria = tokenizer.nextToken(); // usuario
				tokenizer.nextToken(); // base
				arquivoTrajetoria = tokenizer.nextToken(); // arquivo
				dataTrajetoria = tokenizer.nextToken(); // data trajetoria
				latitude = tokenizer.nextToken(); // latitude
				longitude = tokenizer.nextToken(); // longitude
				dataPonto = tokenizer.nextToken(); // data ponto
				horaPonto = tokenizer.nextToken(); // hora ponto
				acuracia = tokenizer.nextToken(); // acuracia

				if (arquivoTrajetoriaAux == null) {
					arquivoTrajetoriaAux = arquivoTrajetoria;
				} else {
					if (!arquivoTrajetoria.equals(arquivoTrajetoriaAux)) {
						trajetoria = new Trajetoria();
						listaPonto = new ArrayList<Ponto>();
						arquivoTrajetoriaAux = arquivoTrajetoria;
					}
				}
			}
		}
	}
}