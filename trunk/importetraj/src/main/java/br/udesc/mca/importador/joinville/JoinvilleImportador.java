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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.matematica.Fisica;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.ponto.PontoDAOPostgreSQL;
import br.udesc.mca.modelo.segmento.Segmento;
import br.udesc.mca.modelo.segmento.SegmentoDAOPostgreSQL;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;
import br.udesc.mca.modelo.usuario.Usuario;
import br.udesc.mca.modelo.usuario.UsuarioDAOPostgreSQL;

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
		String versaoDiferente = prop.getProperty("prob.base.arquivo.versao.diferente").trim().toLowerCase();
		String usuarioDiferente1 = prop.getProperty("prob.base.arquivo.usuario.diferente1").trim().toLowerCase();
		String usuarioDiferente2 = prop.getProperty("prob.base.arquivo.usuario.diferente2").trim().toLowerCase();
		String usuarioDiferente3 = prop.getProperty("prob.base.arquivo.usuario.diferente3").trim().toLowerCase();
		String usuarioModelo = prop.getProperty("prop.base.usuario.modelo").trim().toLowerCase();
		String usuarioAndroid = prop.getProperty("prop.base.usuario.android").trim().toLowerCase();
		String usuarioNome = prop.getProperty("prop.base.usuario.usuario").trim().toLowerCase();
		String usuarioDevice = prop.getProperty("prop.base.usuario.device").trim().toLowerCase();
		String usuarioColetor = prop.getProperty("prop.base.usuario.coletor").trim().toLowerCase();
		String base = prop.getProperty("prop.base.nome");

		Iterator<File> arquivos = null;

		SimpleDateFormat formataDataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd");

		Usuario usuario = null;
		Trajetoria trajetoria = null;
		Ponto ponto = null;
		Ponto pontoAux = null;
		Ponto pontoPrimeiro = null;
		Ponto pontoUltimo = null;
		Segmento segmento = null;
		Coordinate coordenada = null;
		Coordinate coordenadaInversa = null;
		Point pontoGeografico = null;
		Point pontoGeograficoInverso = null;

		List<Ponto> listaPonto = null;
		List<Ponto> listaPontoAux = null;
		Coordinate[] vetorCoordenada = null;
		Coordinate[] vetorCoordenadaInversa = null;

		String latitude = null;
		String longitude = null;
		String altitude = null;
		String velocidade = null;
		String tempoCorrido = null;
		String acuracia = null;
		String bearing = null;
		String ano = null;
		String mes = null;
		String dia = null;
		String hora = null;
		String minuto = null;
		String segundo = null;
		int recursivo = Integer.parseInt(prop.getProperty("prop.base.recursivo"));
		int pontosPorSegmento = Integer.parseInt(prop.getProperty("prop.base.ponto.segmento"));
		int contador = 0;
		long contadorUsuarioDiferente = 0;
		int contadorSoUmPonto = 0;
		int contaPonto = 0;
		int versaoArquivoAndroid = 0;
		int contaVetorCoordenada = 0;
		double lat1 = 0;
		double lon1 = 0;
		double lat2 = 0;
		double lon2 = 0;
		double azimute = 0;
		double comprimento = 0;
		boolean achouUsuarioDiferente = false;

		// iniciando leitura dos arquivos da pasta
		if (recursivo == 0) {
			arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		} else {
			arquivos = FileUtils.iterateFiles(diretorio, ext, true);
		}

		while (arquivos.hasNext()) {
			usuario = new Usuario();
			trajetoria = new Trajetoria();
			listaPonto = new ArrayList<Ponto>();
			versaoArquivoAndroid = 0;
			achouUsuarioDiferente = false;

			File arquivo = arquivos.next();
			FileReader conteudoArquivo = new FileReader(arquivo);
			BufferedReader bufferConteudo = new BufferedReader(conteudoArquivo);
			String linha = null;

			contador++;
			System.out.println("Arquivo: " + contador + "-" + arquivo.getName().trim().toLowerCase());

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

					if (linha.trim().toLowerCase().equals(versaoDiferente)) {
						versaoArquivoAndroid = 404;
					}

					// procurando os usuários que estão fora do padrão 4.0.4
					if (linha.trim().toLowerCase().equals(usuarioDiferente1)
							|| linha.trim().toLowerCase().equals(usuarioDiferente2)
							|| linha.trim().toLowerCase().equals(usuarioDiferente3)) {
						achouUsuarioDiferente = true;
					}

					// procurando os dados do usuário
					if (linha.length() > 8) {
						if (linha.trim().toLowerCase().substring(0, 12).equals(usuarioModelo)) {
							usuario.setModeloCelular(linha.trim().substring(14, linha.length()));
						} else if (linha.trim().toLowerCase().substring(0, 15).equals(usuarioAndroid)) {
							usuario.setAndroid(linha.trim().substring(17, linha.length()));
						} else if (linha.trim().toLowerCase().substring(0, 9).equals(usuarioNome)) {
							usuario.setDescricao(linha.trim().substring(11, linha.length()));
						} else if (linha.trim().toLowerCase().substring(0, 9).equals(usuarioDevice)) {
							usuario.setDevice(linha.trim().substring(11, linha.length()));
						} else if (linha.trim().toLowerCase().substring(0, 15).equals(usuarioColetor)) {
							usuario.setVersaoColetor(linha.trim().substring(17, linha.length()));
						}
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
						acuracia = tokenizer.nextToken(); // Accuracy
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
						// usuario de versão 4.0.4 que o arquivo gerado está
						// fora do padrão sem o bearing
						if (achouUsuarioDiferente && versaoArquivoAndroid == 404) {
							tokenizer.nextToken(); // @
							tokenizer.nextToken(); // Accelerometer_x
							tokenizer.nextToken(); // Accelerometer_y
							tokenizer.nextToken(); // Accelerometer_z
							latitude = tokenizer.nextToken(); // Latitude
							longitude = tokenizer.nextToken(); // Longitude
							altitude = tokenizer.nextToken(); // Altitude
							velocidade = tokenizer.nextToken(); // Speed
							acuracia = tokenizer.nextToken(); // Accuracy
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
							contadorUsuarioDiferente++;
							System.out.println("@Usuario diferente: " + contadorUsuarioDiferente + " "
									+ arquivo.getName().trim().toLowerCase());
						} else {
							tokenizer.nextToken(); // @
							tokenizer.nextToken(); // Accelerometer_x
							tokenizer.nextToken(); // Accelerometer_y
							tokenizer.nextToken(); // Accelerometer_z
							latitude = tokenizer.nextToken(); // Latitude
							longitude = tokenizer.nextToken(); // Longitude
							altitude = tokenizer.nextToken(); // Altitude
							velocidade = tokenizer.nextToken(); // Speed
							acuracia = tokenizer.nextToken(); // Accuracy
							bearing = tokenizer.nextToken(); // Bearing
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
					}

					ponto.setLatitude(Double.parseDouble(latitude));
					ponto.setLongitude(Double.parseDouble(longitude));
					ponto.setAltitude(Double.parseDouble(altitude));
					ponto.setVelocidade(Double.parseDouble(velocidade));
					ponto.setAcuracia(Double.parseDouble(acuracia));
					if (bearing != null) {
						ponto.setBearing(Double.parseDouble(bearing));
					}

					try {
						ponto.setTempo(new Timestamp((formataDataHora
								.parse(ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":" + segundo)
								.getTime())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ponto.setTrajetoria(trajetoria);
					coordenada = new Coordinate(ponto.getLatitude(), ponto.getLongitude());
					coordenadaInversa = new Coordinate(ponto.getLongitude(), ponto.getLatitude());
					pontoGeografico = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
							.createPoint(coordenada);
					pontoGeograficoInverso = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
							.createPoint(coordenadaInversa);
					ponto.setPonto(pontoGeografico);
					ponto.setPontoInverso(pontoGeograficoInverso);
					listaPonto.add(ponto);
				}
			}

			// Trajetórias só com um ponto são descartadas
			if (listaPonto.size() == 1) {
				contadorSoUmPonto++;
				System.out.println("#Só um ponto: " + contadorSoUmPonto + " " + arquivo.getName().trim().toLowerCase());
				continue;
			}

			// gravando no banco
			Session sessao = HibernateUtil.getSessionFactory().openSession();
			Transaction transacao = sessao.beginTransaction();
			UsuarioDAOPostgreSQL usuarioDAOPostgreSQL = new UsuarioDAOPostgreSQL(sessao);
			TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
			PontoDAOPostgreSQL pontoDAOPostgreSQL = new PontoDAOPostgreSQL(sessao);
			SegmentoDAOPostgreSQL segmentoDAOPostgreSQL = new SegmentoDAOPostgreSQL(sessao);

			if (usuario.getDescricao() != null) {
				Query consulta = sessao.getNamedQuery("consultaUsuarioDescricao");

				consulta.setString("descricao", usuario.getDescricao());
				List<Usuario> resultado = consulta.list();

				if (resultado.size() > 0) {
					usuario = resultado.get(0);
				} else {
					usuarioDAOPostgreSQL.inserirUsuario(usuario);
				}
			} else {
				usuarioDAOPostgreSQL.inserirUsuario(usuario);
			}
			trajetoria.setUsuario(usuario);

			trajetoria.setBase(base);
			trajetoria.setArquivo(arquivo.getName().trim().toLowerCase());
			// transforma em segundos
			trajetoria.setDuracao((Double.parseDouble(tempoCorrido) / 1000) % 60);
			try {
				trajetoria.setData(new Date((formataData.parse(ano + "-" + mes + "-" + dia).getTime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			vetorCoordenada = new Coordinate[listaPonto.size()];
			vetorCoordenadaInversa = new Coordinate[listaPonto.size()];
			contaVetorCoordenada = 0;
			for (Ponto pontoLista : listaPonto) {
				coordenada = new Coordinate(pontoLista.getLatitude(), pontoLista.getLongitude());
				coordenadaInversa = new Coordinate(pontoLista.getLongitude(), pontoLista.getLatitude());
				vetorCoordenada[contaVetorCoordenada] = coordenada;
				vetorCoordenadaInversa[contaVetorCoordenada] = coordenadaInversa;
				contaVetorCoordenada++;
			}

			trajetoria.setTrajetoria(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
					.createLineString(vetorCoordenada));
			trajetoria.setTrajetoriaInversa(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
					.createLineString(vetorCoordenadaInversa));

			pontoPrimeiro = listaPonto.get(0);
			pontoUltimo = listaPonto.get(listaPonto.size() - 1);
			comprimento = Azimute.calculaDistanciaKM(pontoPrimeiro.getLatitude(), pontoPrimeiro.getLongitude(),
					pontoUltimo.getLatitude(), pontoUltimo.getLongitude());

			trajetoria.setComprimento(comprimento);
			trajetoria.setVelocidadeMedia(
					Fisica.velocidadeMediaSistemaInternacional(trajetoria.getComprimento(), trajetoria.getDuracao()));

			trajetoriaDAOPostgreSQL.inserirTrajetoria(trajetoria);
			for (Ponto pontoLista : listaPonto) {
				pontoDAOPostgreSQL.inserirPonto(pontoLista);
			}

			// gerando os segmentos
			segmento = new Segmento();
			listaPontoAux = new ArrayList<Ponto>();
			contaPonto = 0;

			for (Ponto pontoLista : listaPonto) {
				if (listaPontoAux.size() == pontosPorSegmento) {
					segmento.setPonto(listaPontoAux);

					for (Ponto pontoListaAux : listaPontoAux) {
						if (contaPonto == 0) {
							lat1 = pontoListaAux.getLatitude();
							lon1 = pontoListaAux.getLongitude();
						} else {
							lat2 = pontoListaAux.getLatitude();
							lon2 = pontoListaAux.getLongitude();
						}
						contaPonto++;
					}
					contaPonto = 0;
					azimute = Azimute.azimute(lat1, lon1, lat2, lon2);
					segmento.setAzimute(azimute);
					segmentoDAOPostgreSQL.inserirSegmento(segmento);
					segmento = new Segmento();
					listaPontoAux = new ArrayList<Ponto>();
					listaPontoAux.add(pontoAux);
				}
				listaPontoAux.add(pontoLista);
				pontoAux = pontoLista;
			}
			if (listaPonto.size() > 1) {
				contaPonto = 0;
				segmento.setPonto(listaPontoAux);
				for (Ponto pontoListaAux : listaPontoAux) {
					if (contaPonto == 0) {
						lat1 = pontoListaAux.getLatitude();
						lon1 = pontoListaAux.getLongitude();
					} else {
						lat2 = pontoListaAux.getLatitude();
						lon2 = pontoListaAux.getLongitude();
					}
					contaPonto++;
				}
				azimute = Azimute.azimute(lat1, lon1, lat2, lon2);
				segmento.setAzimute(azimute);
				segmentoDAOPostgreSQL.inserirSegmento(segmento);
			}

			transacao.commit();
			sessao.close();
			bufferConteudo.close();
			conteudoArquivo.close();
		}
	}
}