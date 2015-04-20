Passo 1- build do projeto trajectory (mvn clean install -DskipTests)
Passo 2- build do projeto microservice (mvn clean install -DskipTests)
Passo 3- ir ao diretorio do servico (eg microservice/kml)
Passo 4- executar (java -jar microservice-XXXXXX-VERSAO.jar server conf.yml)
Passo 5- acessar (http://IP:8080/trajectory-to-kml)






JSON TRAJETORIA
{
    "_id":10
    "description":"Trajectory Desc",
    "originalTrajectory":1,
    "lastModified":1429559053818,
    "versions":[
        {
            "version":1,
            "user":null,
            "previousVersion":null,
            "type":"RAW",
            "lastModified":1429559053819,
            "segments":[{
                "points":[
                    {"lat":36.079548,"lng":-112.25508,"h":0.0,"timestamp":1429559053820},
                    {"lat":36.08117,"lng":-112.25493,"h":0.0,"timestamp":1429559053820},
                    {"lat":36.086494,"lng":-112.26569,"h":0.0,"timestamp":1429559053820}
                ],
                "data":null,
                "transportationMode":"RUN"}
            ],
            "data":null,
            "process":null
        }
    ],
}