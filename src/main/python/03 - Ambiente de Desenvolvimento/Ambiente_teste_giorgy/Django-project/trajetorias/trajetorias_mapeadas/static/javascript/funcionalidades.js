function executa(){
    //Cria mapa
    var configuracoes_do_mapa={
        center: new google.maps.LatLng(0, 0),
        zoom: 2,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    mapa = new google.maps.Map(document.getElementById('mapa'), configuracoes_do_mapa);
    
    // Chama função de criação do poligono 
    var cria_poligono = new PolygonCreator(mapa);
     
    //Limpa seleção no mapa
    $('#apaga_poligono').click(function(){ 
        cria_poligono.destroy();
        cria_poligono=null;
        document.getElementById('submit_passo_1').className = "submit_passo_1_desabilitado";
        cria_poligono=new PolygonCreator(mapa);
    });         
    
    //Mostra coordenadas
    $('#mostrar_coordenadas').click(function mostrar_coordenadas(){ 
        $('#coordenadas_demarcadas').empty();
        if(null==cria_poligono.showData()){
            alert('Você ainda formou o polígono no mapa. Desenho-o e tente novamente!')
        }else{
            $('#coordenadas_demarcadas').append(cria_poligono.showData());
        }
    });

    //Prosseguir
    $('#valida_selecao').click(function valida_selecao(){ 
        $('#coordenadas_demarcadas').empty();
        if(null==cria_poligono.showData()){
            alert('Você ainda formou o polígono no mapa. Desenho-o e tente novamente!')
        }else{
            $('#coordenadas_demarcadas').append(cria_poligono.showData());
            document.getElementById('submit_passo_1').className = "submit_passo_1";
        }
    });
}