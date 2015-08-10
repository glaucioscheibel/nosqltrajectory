package br.udesc.mca.importador.joinville;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.matematica.azimute.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.ponto.PontoDAOPostgreSQL;
import br.udesc.mca.modelo.segmento.Segmento;
import br.udesc.mca.modelo.segmento.SegmentoDAOPostgreSQL;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;

public class JoinvilleImportador {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/joinville.properties");
		props.load(file);
		return props;
	}

	public static void main(String[] args) throws IOException {
		Properties prop = JoinvilleImportador.getProp();
		File diretorio = new File(prop.getProperty("prop.base.caminho"));
		String[] ext = { prop.getProperty("prop.base.extesao") };
		String versaoArquivo = prop.getProperty("prob.base.arquivo.versao.inicial").trim().toLowerCase();
		Iterator<File> arquivos = null;

		SimpleDateFormat formataDataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd");

		Trajetoria trajetoria = null;
		Ponto ponto = null;
		Segmento segmento = null;

		List<Ponto> listaPonto = null;
		Set<Ponto> listaPontoAux = null;

		String latitude = null;
		String longitude = null;
		String altitude = null;
		String velocidade = null;
		String tempoCorrido = null;
		String ano = null;
		String mes = null;
		String dia = null;
		String hora = null;
		String minuto = null;
		String segundo = null;
		int contaPonto = 0;
		int recursivo = Integer.parseInt(prop.getProperty("prop.base.recursivo"));
		int pontosPorSegmento = Integer.parseInt(prop.getProperty("prop.base.ponto.segmento"));
		int contador = 0;
		int versaoArquivoAndroid = 0;
		double lat1 = 0;
		double lon1 = 0;
		double lat2 = 0;
		double lon2 = 0;
		double azimute = 0;

		// iniciando leitura dos arquivos da pasta
		if (recursivo == 0) {
			arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		} else {
			arquivos = FileUtils.iterateFiles(diretorio, ext, true);
		}

		while (arquivos.hasNext()) {
			trajetoria = new Trajetoria();
			listaPonto = new ArrayList<Ponto>();
			versaoArquivoAndroid = 0;

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
					// existem versão diferentes do arquivo que vai ser
					// necessário tratar,
					// inicial é a versão 2.3.6 depois vira 4.0.4
					if (linha.trim().toLowerCase().equals(versaoArquivo)) {
						versaoArquivoAndroid = 2;
					}
					// acho o início dos dados da coleta quebra o laço
					if (linha.trim().equals(prop.getProperty("prop.base.inicio.dados"))) {
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
				if (linha.substring(0, 1).trim().equals(prop.getProperty("prop.base.inicio.registro"))) {
					ponto = new Ponto();
					StringTokenizer tokenizer = new StringTokenizer(linha,
							prop.getProperty("prop.base.separador.dados"));

					if (versaoArquivoAndroid == 2) {
						tokenizer.nextToken(); // @
						tokenizer.nextToken(); // Accelerometer_x
						tokenizer.nextToken(); // Accelerometer_y
						tokenizer.nextToken(); // Accelerometer_z
						latitude = tokenizer.nextToken(); // Latitude
						longitude = tokenizer.nextToken(); // Longitude
						altitude = tokenizer.nextToken(); // Altitude
						velocidade = tokenizer.nextToken(); // Speed
						tokenizer.nextToken(); // Accuracy
						tokenizer.nextToken(); // Battery_%
						ano = tokenizer.nextToken(); // Year
						mes = tokenizer.nextToken(); // Month
						dia = tokenizer.nextToken(); // Day
						hora = tokenizer.nextToken(); // Hour
						minuto = tokenizer.nextToken(); // Minute
						segundo = tokenizer.nextToken(); // Seconds
						tokenizer.nextToken(); // Milliseconds
						// esta informação é da trajetória sempre é gravado
						// o último valor
						tempoCorrido = tokenizer.nextToken(); // Time_since_start_in_ms
					} else {
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
						ano = tokenizer.nextToken(); // Year
						mes = tokenizer.nextToken(); // Month
						dia = tokenizer.nextToken(); // Day
						hora = tokenizer.nextToken(); // Hour
						minuto = tokenizer.nextToken(); // Minute
						segundo = tokenizer.nextToken(); // Seconds
						tokenizer.nextToken(); // Milliseconds
						// esta informação é da trajetória sempre é gravado
						// o último valor
						tempoCorrido = tokenizer.nextToken(); // Time_since_start_in_ms
					}

					ponto.setLatitude(Double.parseDouble(latitude));
					ponto.setLongitude(Double.parseDouble(longitude));
					ponto.setAltitude(Double.parseDouble(altitude));
					ponto.setVelocidade(Double.parseDouble(velocidade));
					try {
						ponto.setTempo(new Timestamp((formataDataHora
								.parse(ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":" + segundo)
								.getTime())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ponto.setTrajetoria(trajetoria);
					listaPonto.add(ponto);

				}
			}

			// gravando no banco
			Session sessao = HibernateUtil.getSessionFactory().openSession();
			Transaction transacao = sessao.beginTransaction();
			TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
			PontoDAOPostgreSQL pontoDAOPostgreSQL = new PontoDAOPostgreSQL(sessao);
			SegmentoDAOPostgreSQL segmentoDAOPostgreSQL = new SegmentoDAOPostgreSQL(sessao);

			trajetoria.setBase(prop.getProperty("prop.base.nome"));
			trajetoria.setArquivo(arquivo.getName().trim().toLowerCase());
			// transforma em segundos
			trajetoria.setDuracao((Double.parseDouble(tempoCorrido) / 1000) % 60);
			try {
				trajetoria.setData(new Date((formataData.parse(ano + "-" + mes + "-" + dia).getTime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			trajetoriaDAOPostgreSQL.inserirTrajetoria(trajetoria);
			for (Ponto pontoLista : listaPonto) {
				pontoDAOPostgreSQL.inserirPonto(pontoLista);
			}

			// gerando os segmentos
			segmento = new Segmento();
			listaPontoAux = new HashSet<Ponto>();
			contaPonto = 0;
			for (Ponto pontoLista : listaPonto) {
				if (contaPonto == pontosPorSegmento) {
					segmento.setPonto(listaPontoAux);
					azimute = Azimute.azimute(lat1, lon1, lat2, lon2);
					segmento.setAzimute(azimute);

					segmentoDAOPostgreSQL.inserirSegmento(segmento);
					segmento = new Segmento();
					listaPontoAux = new HashSet<Ponto>();
					contaPonto = 0;
				}

				if (contaPonto == 0) {
					lat1 = pontoLista.getLatitude();
					lon1 = pontoLista.getLongitude();
				} else {
					if (contaPonto == 1) {
						lat2 = pontoLista.getLatitude();
						lon2 = pontoLista.getLongitude();
					}
				}
				listaPontoAux.add(pontoLista);
				contaPonto++;
			}

			transacao.commit();
			sessao.close();
			bufferConteudo.close();
			conteudoArquivo.close();
			contador++;
			System.out.println("Arquivos: " + contador);
		}

	}

}
