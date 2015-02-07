var poligono = 0
function executa(){
    var configuracoes_do_mapa={
        center: new google.maps.LatLng(0, 0),
        zoom: 2,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    mapa = new google.maps.Map(document.getElementById('mapa'), configuracoes_do_mapa);
    poligono = new PolygonCreator(mapa);
}
function apaga_poligono(){
    poligono.destroy();
    document.getElementById('coordenadas_demarcadas').value='';
    document.getElementById('submit_proximo_passo').className = 'submit_proximo_passo_desabilitado';
    document.getElementById('valida_selecao').className = 'selecao_nao_validada';
    poligono = new PolygonCreator(mapa);
}
function valida_poligono(){
    document.getElementById('coordenadas_demarcadas').value='';
    if(poligono.showData()==null){
        alert('Você ainda formou o polígono no mapa. Desenho-o e tente novamente!');
    }else{
        document.getElementById('coordenadas_demarcadas').value=poligono.showData();
        document.getElementById('submit_proximo_passo').className = 'submit_proximo_passo';
        document.getElementById('valida_selecao').className = 'selecao_validada';
    }
}

function atualiza_percentual_tempo(novo_valor){
    document.getElementById("valor_atual_tempo").innerHTML = novo_valor;
}
function atualiza_percentual_comprimento(novo_valor){
    document.getElementById("valor_atual_comprimento").innerHTML = novo_valor;
}