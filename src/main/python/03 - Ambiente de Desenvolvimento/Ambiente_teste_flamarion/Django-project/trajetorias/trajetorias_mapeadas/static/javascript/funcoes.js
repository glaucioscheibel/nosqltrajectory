//** Função automatica que cria o mapa e chama funções herdeiras**//

var poligono = 0
function executa(passo_atual){
    var configuracoes_do_mapa={
        center: new google.maps.LatLng(0, 0),
        zoom: 2,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    mapa = new google.maps.Map(document.getElementById('mapa'), configuracoes_do_mapa);
    if(passo_atual == 'Passo 1'){
        poligono = new PolygonCreator(mapa);
    }
    else{
        //var pontos = [[-26.392651, -48.734577],[15,5],[23,-5],[10,60],[15,82],[22,-50],[75,-26],[-26,-41],[30,45]];
        trajetorias = document.getElementById('trajetorias').value;
       
        
        //alert(formata_pontos(trajetorias));
        desenha_trajetoria(formata_pontos(trajetorias));
    }
}

function formata_pontos(string){
    // Essa função formata uma string, transformando-a em uma array em JavaScript
    var trajetorias_dados = new Array ();

    // Recebe trajetórias derivadas da busca e inseridas no textarea em formato string
    var trajetorias = (document.getElementById('trajetorias').value).split("}, {");

    //For 1: percorre a lista formada pelo split anterior, que é a separação de todos os dados das trajetórias
    for(a=0; a<trajetorias.length; a++){
        var dados_do_dicionario = (trajetorias[a]).split("'pontos': ");

        var pontos = new Array ();
        //For 2: percorre apenas a segunda parte (os pontos) de cada tupla formada pelo split anterior.
        for(b=0; b<(dados_do_dicionario.length)/2; b++){
            var lista_de_pontos = (dados_do_dicionario[b*2+1]).split("), (");

            //For 3: percorre cada ponto para dividir os dados em Latitude e Longitude
            for(c=0; c<lista_de_pontos.length; c++){
                coordenadas = (lista_de_pontos[c]).split("', '");

                var latitude = parseFloat(remove_caracteres_especiais(coordenadas[0]));
                var longitude = parseFloat(remove_caracteres_especiais(coordenadas[1]));
                
                //Adiciona tupla latitude&longitude a lista de pontos
                pontos[pontos.length] = [latitude,longitude];
            }
        }

        trajetorias_dados[trajetorias_dados.length] = pontos;
    }
    return trajetorias_dados;
}

function remove_caracteres_especiais(string){
    var nova_string = (((string.replace("'","")).replace("[(","")).replace(")]","")).replace("}]","");
    return nova_string;
}

//** Apaga poligono desenhado **//
function apaga_poligono(){
    poligono.destroy();
    document.getElementById('coordenadas_demarcadas').value = '';
    document.getElementById('submit_proximo_passo').className = 'submit_proximo_passo_desabilitado';
    document.getElementById('valida_selecao').className = 'selecao_nao_validada';
    poligono = new PolygonCreator(mapa);
}

//** Valida a área selecionada (poligono) **//
function valida_poligono(){
    document.getElementById('coordenadas_demarcadas').value = '';
    if(poligono.showData() == null){
        alert('Você ainda formou o polígono no mapa. Desenho-o e tente novamente!');
    }else{
        document.getElementById('coordenadas_demarcadas').value = poligono.showData();
        document.getElementById('submit_proximo_passo').className = 'submit_proximo_passo';
        document.getElementById('valida_selecao').className = 'selecao_validada';
    }
}

//** Desenha trajetórias no mapa **//
function desenha_trajetoria(pontos){
    cores = [
        '#000000',
        '#FFFFFF',
        '#FF0000',
        '#1E90FF',
        '#008000',
        '#800080',
        '#FFFF00',
        '#FFA500',
        '#A52A2A',
        '#0000FF',
        '#90EE90',
        '#FFC0CB',
        '#4D4D4D',
        '#BEBEBE',
        '#FF5B00'
    ];
    
    for (var y = 0; y<pontos.length; y++){

        // Formula uma lista com Lat e Lon de cada ponto da trajetória.
        var pontos_da_trajetoria = new Array ();
        for (var x=0; x<pontos[y].length; x++){
            proximo = pontos_da_trajetoria.length;
            pontos_da_trajetoria[proximo] = new google.maps.LatLng(pontos[y][x][0],pontos[y][x][1]);
        }
        
        // Define uma cor aleatória para representar a trajetória.
        cor = cores[Math.floor(Math.random() * (cores.length))];
        
        // Desenha trajetória no mapa.
        var trajetoria = new google.maps.Polyline({
            path: pontos_da_trajetoria,
            geodesic: false,
            strokeColor: cor,
            strokeOpacity: 1.0,
            strokeWeight: 2
        });

        trajetoria.setMap(mapa);

        // Introduz ponto inicial e final da trajetória no mapa.
        var ponto_inicial_e_final = [
            [pontos[y][0][0],pontos[y][0][1]],
            [pontos[y][pontos[y].length-1][0],pontos[y][pontos[y].length-1][1]]
        ]
        marca_ponto_inicial_e_final(ponto_inicial_e_final);
    }
}
function atualiza_percentual_tempo(novo_valor){
    document.getElementById("valor_atual_tempo").innerHTML = novo_valor;
}
function atualiza_percentual_comprimento(novo_valor){
    document.getElementById("valor_atual_comprimento").innerHTML = novo_valor;
}
function marca_ponto_inicial_e_final(pontos){
    for(z=0; z<2; z++){
        if(z==0){
            titulo = "Ponto Inicial";
        }
        else{
            titulo = "Ponto Final";
        }
        ponto = new google.maps.LatLng(pontos[z][0],pontos[z][1]);
        var ponto = new google.maps.Marker({
            position: ponto,
            title: titulo,
        });
        ponto.setMap(mapa);
    }
}
function altera_estado(par){
    if(document.getElementById(par).className == "par_nao_selecionado"){
        document.getElementById(par).className = "par_selecionado";
    }
    else{
        document.getElementById(par).className = "par_nao_selecionado";   
    }
}
function selecionar_tudo(){
    for(var i=0;i<document.form3.elements.length;i++){
        var x = document.form3.elements[i];
        if (x.name == 'trajetorias_similares') { 
            x.checked = document.form3.selecionar_tudo_nada.checked;
            if(document.form3.selecionar_tudo_nada.checked == true){
                document.getElementById('selecionar_tudo_nada').className = "tudo_selecionado";
            }
            else{
                document.getElementById('selecionar_tudo_nada').className = "nada_selecionado";
            }
        } 
    }
}