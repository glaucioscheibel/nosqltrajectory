<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/theme.css">
<link type="text/css" rel="stylesheet" href="css/semantic.css">
<title>Início - TrajeSim</title>
</head>
<body class="azul">

	<div class="ui grid centered">
		<div class="row azul">
			<div class="twelve wide column azul">
				<img class="ui image" src="imagens/logo.png">
			</div>
		</div>

		<div class="row white">

			<div class="six wide column aligned center">

				<form class="ui form" method="POST" action="/trajetorias/2/">
					<!--  {% csrf_token %} @todo tirar os comentários aqui depois-->

					<div class="two fields">
						<div class="field">
							<label>Data Inicial</label> <input type="text" name="data_inicio"
								id="inicio" maxlength="10" pattern="\d{1,2}/\d{1,2}/\d{4}"
								value="1/1/2000" placeholder="dd/mm/aaaa"
								title="Insira uma data no formato dd/mm/aaaa" required>
						</div>

						<div class="field">
							<label>Data Final</label> <input type="text" name="data_fim"
								id="fim" maxlength="10" pattern="\d{1,2}/\d{1,2}/\d{4}"
								placeholder="dd/mm/aaaa"
								title="Insira uma data no formato dd/mm/aaaa" value="" required>
						</div>
						<input type="submit" value="Continuar"
							class="ui submit button azul">
					</div>

				</form>

			</div>

		</div>

		<div class="row white centro">
			<div class="ui twelve wide column galeria">
				<div class="ui move reveal azul">
					<img class="ui image visible content" src="imagens/inicio.jpg">
					<img class="ui image hidden content" src="imagens/bem-vindo.jpg">
				</div>
			</div>

		</div>

		<div class="row azul footer">
			<div class="twelve wide column aligned center">
				<span class="text">TrajeSim é um projeto desenvolvido em <b><a
						href="http://fabricadesoftware.araquari.ifc.edu.br/">Fábrica
							de Software</a></b></span>
				<div class="ui divider"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery214.js"></script>
	<script type="text/javascript" src="js/semantic.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
</body>
</html>