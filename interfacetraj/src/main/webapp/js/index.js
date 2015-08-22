
//  Funções utilizadas na primeira tela do APP.
//      # Galeria
//      # Verificação do campo data

var data_atual;
var data_atual_string;

$('document').ready(function(){
    setInterval(trocaImagem, 3000);
    
    data_atual = new Date();
    data_atual_string = converteDataParaString(data_atual);
    
    $('#fim').val(data_atual_string);

    $('#inicio').change(function(){
        data = $(this).val();
        validaData(data,'inicio');
    })

    $('#fim').change(function(){
        data = $(this).val();
        validaData(data,'fim');
    })

    $('.form_data').submit(function (){

        inicio = $('#inicio').val();
        fim = $('#fim').val();
        
        validaData(inicio,'inicio');
        validaData(fim,'fim');

        if(new Date(converteStringParaDate(inicio)) > new Date(converteStringParaDate(fim))){
            alert("A Data inicial não pode ser maior que a final!");
            $('#inicio').val($('#fim').val());
            return false;
        }
    })

})

function validaData(data,periodo){
    if(data.split("/").length == 3){

        var data_inserida = converteStringParaDate(data);

        if(data_inserida == "Invalid Date"){
            alert("Data inválida. Por favor, reensira uma nova data!");
            $('#'+periodo).val("");
            return false;
        }
        else{
            $('#'+periodo).val(converteDataParaString(data_inserida));
        
            dia = data_inserida.getDate();
            mes = data_inserida.getMonth()+1;
            ano = data_inserida.getFullYear();

            if(data_inserida > data_atual){
                alert("Não é possível inserir uma data maior que a atual!");
                $('#'+periodo).val(data_atual_string);
                return false;
            }
        }
    }
}

function converteStringParaDate(data){
    array_data = data.split("/");
    
    dia = parseInt(array_data[0]);
    mes = parseInt(array_data[1]);
    ano = parseInt(array_data[2]);
    
    data_formatada = new Date(ano,mes-1,dia);
    
    return data_formatada;
}

var atual = 2;
var anterior = 1;

function trocaImagem(){
    $('#img_'+atual).css("opacity",1);
    $('#img_'+anterior).css("opacity",0);

    $('#txt_'+atual).css("opacity",1);
    $('#txt_'+anterior).css("opacity",0);            
    
    anterior = atual;
    atual += 1;

    if(atual > 3){
        atual = 1;
    }
}

function converteDataParaString(data){
    dia = data.getDate();
    mes = data.getMonth()+1;
    ano = data.getFullYear();

    data_string = dia+"/"+mes+"/"+ano
    //alert(data_string);
    return data_string;
}