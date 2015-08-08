package br.udesc.mca.importador.joinville;

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
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.ponto.PontoDAOPostgreSQL;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;

public class JoinvilleImportador {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/dados.properties");
		props.load(file);
		return props;
	}

	public static void main(String[] args) throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties prop = JoinvilleImportador.getProp();

		File diretorio = new File(prop.getProperty("prop.base.caminho"));
		String[] ext = { prop.getProperty("prop.base.extesao") };
		Iterator<File> arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		Trajetoria trajetoria = null;
		Ponto ponto = null;
		List<Ponto> listaPonto = new ArrayList<Ponto>();

		// iniciando leitura dos arquivos da pasta
		while (arquivos.hasNext()) {
			trajetoria = new Trajetoria();
			String latitude = null;
			String longitude = null;
			String altitude = null;
			String velocidade = null;
			String tempoCorrido = null;

			File arquivo = arquivos.next();

			FileReader conteudoArquivo = new FileReader(arquivo);
			BufferedReader bufferConteudo = new BufferedReader(conteudoArquivo);

			String linha = null;
			// ignora linhas iniciais do arquivo
			while ((linha = bufferConteudo.readLine()) != null) {
				// pular linhas em branco
				if (linha.trim().length() == 0) {
					continue;
				} else {
					// acho o inicio da coleta quebra o laço
					if (linha.trim().equals("-----")) {
						break;
					}
				}
			}

			while ((linha = bufferConteudo.readLine()) != null) {
				// pular linhas em branco
				if (linha.trim().length() == 0) {
					continue;
				}

				// procurando somente linhas com @ na frente, o @ indica a
				// coleta de ponto
				if (linha.substring(0, 1).trim().equals("@")) {
					ponto = new Ponto();
					StringTokenizer tokenizer = new StringTokenizer(linha, ";");

					tokenizer.nextToken(); // @
					tokenizer.nextToken(); // Accelerometer_x
					tokenizer.nextToken(); // Accelerometer_y
					tokenizer.nextToken(); // Accelerometer_z
					latitude = tokenizer.nextToken(); // Latitude
					longitude = tokenizer.nextToken(); // Longitude
					altitude = tokenizer.nextToken(); // Altitude
					velocidade = tokenizer.nextToken(); // Speed
					tokenizer.nextToken(); // Accuracy
					tokenizer.nextToken(); // Bearing
					tokenizer.nextToken(); // Battery_%
					tokenizer.nextToken(); // Year
					tokenizer.nextToken(); // Month
					tokenizer.nextToken(); // Day
					tokenizer.nextToken(); // Hour
					tokenizer.nextToken(); // Minute
					tokenizer.nextToken(); // Seconds
					tokenizer.nextToken(); // Milliseconds
					// esta informação é da trajetória sempre é gravado o último
					// valor
					tempoCorrido = tokenizer.nextToken(); // Time_since_start_in_ms

					ponto.setLatitude(Double.parseDouble(latitude));
					ponto.setLongitude(Double.parseDouble(longitude));
					ponto.setAltitude(Double.parseDouble(altitude));
					ponto.setVelocidade(Double.parseDouble(velocidade));
					listaPonto.add(ponto);
				}
			}

			Session sessao = HibernateUtil.getSessionFactory().openSession();
			Transaction transacao = sessao.beginTransaction();
			TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
			PontoDAOPostgreSQL pontoDAOPostgreSQL = new PontoDAOPostgreSQL(sessao);

			trajetoria.setBase(prop.getProperty("prop.base.nome"));
			trajetoria.setArquivo(arquivo.getName().trim().toLowerCase());
			trajetoria.setDuracao(Double.parseDouble(tempoCorrido));
			trajetoriaDAOPostgreSQL.inserirTrajetoria(trajetoria);
			
			for (Ponto pontoLista : listaPonto) {
				pontoDAOPostgreSQL.inserirPonto(pontoLista);
			}			
			transacao.commit();

			bufferConteudo.close();
			conteudoArquivo.close();
		}

	}

}
