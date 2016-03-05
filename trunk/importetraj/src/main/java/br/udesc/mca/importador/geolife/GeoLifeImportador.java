package br.udesc.mca.importador.geolife;

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
import br.udesc.mca.importador.importador.Importador;
import br.udesc.mca.importador.tdrive.TDriveImportador;
import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.matematica.Fisica;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.ponto.PontoDAOPostgreSQL;
import br.udesc.mca.modelo.segmento.Segmento;
import br.udesc.mca.modelo.segmento.SegmentoDAOPostgreSQL;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;
import br.udesc.mca.modelo.transporte.Transporte;
import br.udesc.mca.modelo.transporte.TransporteDAOPostgreSQL;
import br.udesc.mca.modelo.usuario.Usuario;
import br.udesc.mca.modelo.usuario.UsuarioDAOPostgreSQL;

public class GeoLifeImportador extends Importador {
	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/geolife.properties");
		props.load(file);
		return props;
	}

	public static void main(String[] args) throws IOException {
		Properties prop = TDriveImportador.getProp();
		File diretorio = new File(prop.getProperty("prop.base.caminho"));
		String[] ext = { prop.getProperty("prop.base.extesao") };
		Session sessao = HibernateUtil.getSessionFactory().openSession();

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
		Transporte transporte = new Transporte();

		List<Ponto> listaPonto = null;
		List<Ponto> listaPontoAux = null;
		Coordinate[] vetorCoordenada = null;
		Coordinate[] vetorCoordenadaInversa = null;

		String base = prop.getProperty("prop.base.nome");
		String arquivoTrajetoria = null;
		String latitude = null;
		String longitude = null;
		String altura = null;
		String dataTrajetoria = null;
		String horaTrajetoria = null;
		String arquivoTrajetoriaAux = null;

		String ano = null;
		String mes = null;
		String dia = null;
		String hora = null;
		String minuto = null;
		String segundo = null;

		int recursivo = Integer.parseInt(prop.getProperty("prop.base.recursivo"));
		int pontosPorSegmento = Integer.parseInt(prop.getProperty("prop.base.ponto.segmento"));
		long contaLinha = 0;
		int contaVetorCoordenada = 0;
		int contaPonto = 0;
		int contadorSoUmPonto = 0;

		double lat1 = 0;
		double lon1 = 0;
		double lat2 = 0;
		double lon2 = 0;
		double azimute = 0;
		double comprimento = 0;
		double alturaBanco = 0;

		// iniciando leitura dos arquivos da pasta
		if (recursivo == 0) {
			arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		} else {
			arquivos = FileUtils.iterateFiles(diretorio, ext, true);
		}

		try {
			while (arquivos.hasNext()) {
				trajetoria = new Trajetoria();
				usuario = new Usuario();

				listaPonto = new ArrayList<Ponto>();
				File arquivo = arquivos.next();
				FileReader conteudoArquivo = new FileReader(arquivo);
				BufferedReader bufferConteudo = new BufferedReader(conteudoArquivo);
				String linha = null;
				pontoAux = null;

				System.out.println("Arquivo - " + arquivo.getName().trim().toLowerCase());

				for (int i = 1; i <= 6; i++) {
					bufferConteudo.readLine();
				}

				while ((linha = bufferConteudo.readLine()) != null) {
					// pular linhas em branco
					if (linha.trim().length() == 0) {
						continue;
					}

					contaLinha++;
					System.out.println("Linha - " + contaLinha);
					ponto = new Ponto();

					// 39.9851333333333,116.347483333333,0,219.816272965879,39202.0375578704,2007-04-30,00:54:05
					// latitude;longitude;nao defenido;altitude;data
					// corrida;data;hora
					StringTokenizer tokenizer = new StringTokenizer(linha,
							prop.getProperty("prop.base.separador.dados"));

					arquivoTrajetoria = arquivo.getName();

					latitude = tokenizer.nextToken(); // latitude
					longitude = tokenizer.nextToken(); // longitude

					tokenizer.nextToken(); // 0 por padrão não definido

					altura = tokenizer.nextToken();
					alturaBanco = Double.parseDouble(altura);

					dataTrajetoria = tokenizer.nextToken(); // data trajetoria

					if (arquivoTrajetoriaAux == null) {
						arquivoTrajetoriaAux = arquivoTrajetoria;
					}

					StringTokenizer dataTokenizer = new StringTokenizer(dataTrajetoria,
							prop.getProperty("prop.base.separador.data"));
					ano = dataTokenizer.nextToken();
					ano = ano.substring(0, 4).trim();
					mes = dataTokenizer.nextToken();
					dia = dataTokenizer.nextToken();
					dia = dia.substring(0, 2).trim();

					if (dia.length() == 1) {
						dia = "0" + dia;
					}

					if (mes.length() == 1) {
						mes = "0" + mes;
					}

					horaTrajetoria = tokenizer.nextToken();
					StringTokenizer horaTokenizer = new StringTokenizer(horaTrajetoria,
							prop.getProperty("prop.base.separador.hora"));

					hora = horaTokenizer.nextToken();
					minuto = horaTokenizer.nextToken();
					segundo = horaTokenizer.nextToken();

					if (hora.length() == 1) {
						hora = "0" + hora;
					}

					if (minuto.length() == 1) {
						minuto = "0" + minuto;
					}

					if (segundo.length() == 1) {
						segundo = "0" + segundo;
					}

					try {
						ponto.setTempo(new Timestamp((formataDataHora
								.parse(ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":" + segundo)
								.getTime())));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					ponto.setTrajetoria(trajetoria);
					ponto.setLatitude(Double.parseDouble(latitude));
					ponto.setLongitude(Double.parseDouble(longitude));

					coordenada = new Coordinate(ponto.getLatitude(), ponto.getLongitude());
					coordenadaInversa = new Coordinate(ponto.getLongitude(), ponto.getLatitude());

					pontoGeografico = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
							.createPoint(coordenada);
					pontoGeograficoInverso = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
							.createPoint(coordenadaInversa);

					ponto.setPonto(pontoGeografico);
					ponto.setPontoInverso(pontoGeograficoInverso);
					if (pontoAux == null) {
						ponto.setVelocidade(0d);
					} else {
						ponto.setVelocidade(Fisica.velocidadeMediaSistemaInternacionalMPSPonto(pontoAux, ponto, true));
					}
					pontoAux = ponto;
					listaPonto.add(ponto);
				}
				// Trajetórias só com um ponto são descartadas
				if (listaPonto.size() == 1) {
					contadorSoUmPonto++;
					System.out.println(
							"#Só um ponto: " + contadorSoUmPonto + " " + arquivo.getName().trim().toLowerCase());
					continue;
				}

				// gravando no banco
				Transaction transacao = sessao.beginTransaction();
				UsuarioDAOPostgreSQL usuarioDAOPostgreSQL = new UsuarioDAOPostgreSQL(sessao);
				TransporteDAOPostgreSQL transporteDAOPostgreSQL = new TransporteDAOPostgreSQL(sessao);
				TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
				PontoDAOPostgreSQL pontoDAOPostgreSQL = new PontoDAOPostgreSQL(sessao);
				SegmentoDAOPostgreSQL segmentoDAOPostgreSQL = new SegmentoDAOPostgreSQL(sessao);

				if (usuario.getDescricao() != null) {
					Query consulta = sessao.getNamedQuery("consultaUsuarioDescricao");

					consulta.setString("descricao", usuario.getDescricao().trim());
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

				if (arquivo.getName().substring(1, 3).equals(Transporte.TipoTransporte.TAXI.name().substring(1, 3))) {
					transporte.setDescricao(Transporte.TipoTransporte.TAXI.name());
				} else if (arquivo.getName().substring(1, 3)
						.equals(Transporte.TipoTransporte.BUS.name().substring(1, 3))) {
					transporte.setDescricao(Transporte.TipoTransporte.BUS.name());
				} else if (arquivo.getName().substring(1, 3)
						.equals(Transporte.TipoTransporte.CAR.name().substring(1, 3))) {
					transporte.setDescricao(Transporte.TipoTransporte.CAR.name());
				}

				if (transporte.getDescricao() != null) {
					Query consulta = sessao.getNamedQuery("consultaTransporteDescricao");

					consulta.setString("descricao", transporte.getDescricao().trim());
					List<Transporte> resultado = consulta.list();

					if (resultado.size() > 0) {
						transporte = resultado.get(0);
					} else {
						transporteDAOPostgreSQL.inserirTransporte(transporte);
					}
				} else {
					transporteDAOPostgreSQL.inserirTransporte(transporte);
				}
				trajetoria.setTransporte(transporte);

				trajetoria.setBase(base);
				trajetoria.setArquivo(arquivo.getName().trim().toLowerCase());

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
				comprimento = Azimute.calculaDistanciaMetros(pontoPrimeiro.getLatitude(), pontoPrimeiro.getLongitude(),
						pontoUltimo.getLatitude(), pontoUltimo.getLongitude(), true);

				trajetoria.setComprimento(comprimento);
				trajetoria.setDuracao(
						new Double(Fisica.duracaoSegundos(pontoPrimeiro.getTempo(), pontoUltimo.getTempo(), true)));
				trajetoria.setVelocidadeMedia(
						Fisica.velocidadeMediaSistemaInternacionalMPSPonto(pontoPrimeiro, pontoUltimo, true));

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
				System.out.println("#Comitou Arquivo - " + arquivo.getName().trim().toLowerCase());
				sessao.flush();
				transacao.commit();
				sessao.clear();
				bufferConteudo.close();
				conteudoArquivo.close();

				// limpando a memória
				usuario = null;
				trajetoria = null;
				ponto = null;
				pontoAux = null;
				pontoPrimeiro = null;
				pontoUltimo = null;
				segmento = null;
				coordenada = null;
				coordenadaInversa = null;
				pontoGeografico = null;
				pontoGeograficoInverso = null;
				listaPonto = null;
				listaPontoAux = null;
				vetorCoordenada = null;
				vetorCoordenadaInversa = null;
				System.gc();
			}
		} catch (Exception e) {
			System.out.println("#Erro: " + e.getMessage());
		}
		sessao.close();
	}
}
