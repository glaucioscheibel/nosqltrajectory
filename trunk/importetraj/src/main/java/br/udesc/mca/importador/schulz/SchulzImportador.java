package br.udesc.mca.importador.schulz;

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
import br.udesc.mca.modelo.transporte.Transporte;
import br.udesc.mca.modelo.transporte.TransporteDAOPostgreSQL;
import br.udesc.mca.modelo.usuario.Usuario;
import br.udesc.mca.modelo.usuario.UsuarioDAOPostgreSQL;

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

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;

		PontoDAOPostgreSQL pontoDAOPostgreSQL = new PontoDAOPostgreSQL(sessao);
		SegmentoDAOPostgreSQL segmentoDAOPostgreSQL = new SegmentoDAOPostgreSQL(sessao);

		Usuario usuario = new Usuario();
		UsuarioDAOPostgreSQL usuarioDAOPostgreSQL = new UsuarioDAOPostgreSQL(sessao);

		Trajetoria trajetoria = null;
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);

		Transporte transporte = new Transporte();
		TransporteDAOPostgreSQL transporteDAOPostgreSQL = new TransporteDAOPostgreSQL(sessao);

		transporte.setDescricao(Transporte.TipoTransporte.CAR.name());

		Ponto ponto = null;
		Ponto pontoAux = null;
		Ponto pontoPrimeiro = null;
		Ponto pontoUltimo = null;
		Segmento segmento = null;

		List<Ponto> listaPonto = null;
		List<Ponto> listaPontoAux = null;
		Coordinate[] vetorCoordenada = null;
		Coordinate[] vetorCoordenadaInversa = null;

		Coordinate coordenada = null;
		Coordinate coordenadaInversa = null;
		Point pontoGeografico = null;
		Point pontoGeograficoInverso = null;

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
		String meioTransporte = null;

		String ano = null;
		String mes = null;
		String dia = null;
		String hora = null;
		String minuto = null;
		String segundo = null;

		int recursivo = Integer.parseInt(prop.getProperty("prop.base.recursivo"));
		int pontosPorSegmento = Integer.parseInt(prop.getProperty("prop.base.ponto.segmento"));
		int contaLinha = 0;
		int contaVetorCoordenada = 0;
		int contaPonto = 0;

		double lat1 = 0;
		double lon1 = 0;
		double lat2 = 0;
		double lon2 = 0;
		double azimute = 0;
		double comprimento = 0;

		// iniciando leitura dos arquivos da pasta
		if (recursivo == 0) {
			arquivos = FileUtils.iterateFiles(diretorio, ext, false);
		} else {
			arquivos = FileUtils.iterateFiles(diretorio, ext, true);
		}

		while (arquivos.hasNext()) {
			trajetoria = new Trajetoria();
			trajetoria.setTransporte(transporte);
			listaPonto = new ArrayList<Ponto>();
			File arquivo = arquivos.next();
			FileReader conteudoArquivo = new FileReader(arquivo);
			BufferedReader bufferConteudo = new BufferedReader(conteudoArquivo);
			String linha = null;

			System.out.println("Arquivo - " + arquivo.getName().trim().toLowerCase());

			while ((linha = bufferConteudo.readLine()) != null) {
				// pular linhas em branco
				if (linha.trim().length() == 0) {
					continue;
				}

				contaLinha++;
				System.out.println("Linha - " + contaLinha);
				ponto = new Ponto();

				// usuario;base;arquivo;datat;lat;lon;datap;horap;acuracia
				StringTokenizer tokenizer = new StringTokenizer(linha, prop.getProperty("prop.base.separador.dados"));

				usuarioTrajetoria = tokenizer.nextToken(); // usuario
				tokenizer.nextToken(); // base
				arquivoTrajetoria = tokenizer.nextToken(); // arquivo
				dataTrajetoria = tokenizer.nextToken(); // data trajetoria

				StringTokenizer dataTokenizer = new StringTokenizer(dataTrajetoria,
						prop.getProperty("prop.base.separador.data"));
				dia = dataTokenizer.nextToken();
				mes = dataTokenizer.nextToken();
				ano = dataTokenizer.nextToken();

				if (dia.length() == 1) {
					dia = "0" + dia;
				}

				if (mes.length() == 1) {
					mes = "0" + mes;
				}

				latitude = tokenizer.nextToken(); // latitude
				longitude = tokenizer.nextToken(); // longitude
				dataPonto = tokenizer.nextToken(); // data ponto
				horaPonto = tokenizer.nextToken(); // hora ponto

				StringTokenizer horaTokenizer = new StringTokenizer(horaPonto,
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

				acuracia = tokenizer.nextToken();

				ponto.setLatitude(Double.parseDouble(latitude));
				ponto.setLongitude(Double.parseDouble(longitude));
				ponto.setAcuracia(Double.parseDouble(acuracia));

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
					ponto.setVelocidade(Fisica.velocidadeMediaSistemaInternacionalPonto(pontoAux, ponto));
				}

				ponto.setTrajetoria(trajetoria);

				listaPonto.add(ponto);
				pontoAux = ponto;

				if (arquivoTrajetoriaAux == null) {
					arquivoTrajetoriaAux = arquivoTrajetoria;
				} else {
					if (!arquivoTrajetoria.equals(arquivoTrajetoriaAux)) {

						// trajetórias com 1 ponto são descartadas
						if (listaPonto.size() == 1) {
							continue;
						} else {
							transacao = sessao.beginTransaction();

							try {
								trajetoria
										.setData(new Date((formataData.parse(ano + "-" + mes + "-" + dia).getTime())));
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

							trajetoria.setTrajetoria(
									new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
											.createLineString(vetorCoordenada));
							trajetoria.setTrajetoriaInversa(
									new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
											.createLineString(vetorCoordenadaInversa));

							trajetoria.setBase(base);
							trajetoria.setArquivo(arquivoTrajetoriaAux);

							pontoPrimeiro = listaPonto.get(0);
							pontoUltimo = listaPonto.get(listaPonto.size() - 1);
							comprimento = Azimute.calculaDistanciaKM(pontoPrimeiro.getLatitude(),
									pontoPrimeiro.getLongitude(), pontoUltimo.getLatitude(),
									pontoUltimo.getLongitude());

							trajetoria.setComprimento(comprimento);

							trajetoria.setVelocidadeMedia(
									Fisica.velocidadeMediaSistemaInternacionalPonto(pontoPrimeiro, pontoUltimo));

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

							trajetoria = new Trajetoria();
							listaPonto = new ArrayList<Ponto>();
							arquivoTrajetoriaAux = arquivoTrajetoria;

							usuario.setDescricao(usuarioTrajetoria);
							trajetoria.setTransporte(transporte);
							trajetoria.setUsuario(usuario);

							pontoAux = null;

							transacao.commit();

						}
					}
				}
			}
		}
		sessao.close();
	}
}