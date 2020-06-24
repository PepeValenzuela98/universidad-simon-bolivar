import {initCarrousel} from  './Carrusel.js';
import {initValidations} from  './Validations.js';

var slide = false;

var slideFilterForm = false;

var listAuthDropDown = false;
var listAuth = document.getElementById("listAuth");
var buttonAuth = document.getElementById("buttonAuth");

if (document.getElementById("listAuth")) {
    buttonAuth.onclick = function () {
        listAuthDropDown = !listAuthDropDown;
        if (listAuthDropDown) {
            listAuth.className = "listAuthInfo";
        } else {
            listAuth.className = "listAuthInfoDisable";
        }
    };
}



if (document.getElementById("filterHeader")) {
    document.getElementById("filterHeader").onclick = function () {
        slideFilterForm = !slideFilterForm;
        if (slideFilterForm) {
            document.getElementById("filterForm").className = "filterFormShow";
            document.getElementById("arrowFilterIcon").src = "https://img.icons8.com/ios-filled/50/000000/up-squared--v1.png";
        } else {
            document.getElementById("filterForm").className = "filterFormHide";
            document.getElementById("arrowFilterIcon").src = "https://img.icons8.com/ios-filled/50/000000/down-squared--v1.png";
        }
    };
}


document.getElementById("menuMobile").onclick = function () {
    slide = !slide;
    if (slide) {
        document.getElementById("opciones").className = "slideDown";
    } else {
        document.getElementById("opciones").className = "slideUp";
    }
};

if (document.getElementById("generar")){
    document.getElementById("generar").onmouseenter = function () {
        document.getElementById("generarContent").className = "generarContentEnter";
    };

    document.getElementById("generarContent").addEventListener("mouseover", function () {
        document.getElementById("generarContent").className = "generarContentEnter";
    });

    document.getElementById("generarContent").addEventListener("mouseout", function () {
        document.getElementById("generarContent").className = "generarContentOut";
    });


    document.getElementById("generar").onmouseout = function () {
        document.getElementById("generarContent").className = "generarContentOut";

    };
}


if (document.title === "Universidad Simon Bolivar - Inicio") {
    initCarrousel();
}

initValidations();



if (document.getElementById("seleccionarMateria")) {
    let inputMateria = document.getElementById("seleccionarMateria");
    inputMateria.onkeyup = function () {
        let nombreMateria = inputMateria.value;
        document.getElementById("nombresDeMateria").innerHTML = "";
        document.getElementById("nombresDeMateria").style = "overflow: none;height: auto;";
        if (inputMateria.value !== "") {
            ajaxMaterias(nombreMateria);

        }
    };
}

function ajaxMaterias(nombreMateria) {
    var xhttp = new XMLHttpRequest();
    var url = "/grupos/cargar-materias/" + nombreMateria;
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            var materias = JSON.parse(this.responseText);
            var nombreDeMateriaHTML = "";
            for (var materia in materias) {
                nombreDeMateriaHTML += "<label class=\"nombreDeCadaMateria\">" + materias[materia].codigoMateria + '-' + materias[materia].nombre + "</label>";
            }
            if (nombreDeMateriaHTML) {
                document.getElementById("nombresDeMateria").innerHTML = nombreDeMateriaHTML;
                document.getElementById("nombresDeMateria").style = "overflow: scroll;height: 100px;";
                Array.from(document.getElementsByClassName("nombreDeCadaMateria")).forEach(function (element) {
                    element.addEventListener('click', function (event) {
                        var infoMateria = element.innerHTML.split("-");
                        document.getElementById("seleccionarCodigoMateria").value = infoMateria[0];
                        document.getElementById("seleccionarMateria").value = infoMateria[1];
                        document.getElementById("nombresDeMateria").innerHTML = "";
                        document.getElementById("nombresDeMateria").style = "overflow: none;height: auto;";
                    });
                });
            }
        }
    };
    xhttp.open("GET", url, true);
    xhttp.send();
}

if (document.getElementById("buscarKardex") && document.getElementById("selectKardex")) {
    let buscarKardex = document.getElementById("buscarKardex");
    buscarKardex.onclick = () => {
        ajaxKardex();
    };
}

function ajaxKardex() {
    var xhttp = new XMLHttpRequest();
    let selectKardex = document.getElementById("selectKardex");
    let valueSelected = selectKardex.options[selectKardex.selectedIndex].value;
    let numeroDeControlAlumno = document.getElementById("numeroDeControlAlumno").value;
    let url = "/kardex/cargar-kardex/" + numeroDeControlAlumno + "/" + valueSelected;

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            var kardex = JSON.parse(this.responseText);
            var kardexHTML = "";
            var semestre = kardex[0].semestreCurso;
            var contador = 1;
            var promedioPorSemestre = [];

            for (var k in kardex) {
                if (contador === 1) {
                    kardexHTML += "<table style='margin-top:25px;'><h1>Semestre " + semestre + "<h1>\n\
                              <thead><tr><th>Materia</th><th>Calificacion</th><th>Evaluacion</th></tr></thead><tbody>";
                }
                if (semestre !== kardex[k].semestreCurso) {
                    var promedio = promedioPorSemestre.reduce((a, b) => a + b, 0) / promedioPorSemestre.length;
                    kardexHTML += "<tr><td></td><td>Promedio:</td><td>" + promedio + "</td></tr></tbody></table>";
                    semestre = kardex[k].semestreCurso;
                    promedioPorSemestre = [];
                    kardexHTML += "<table style='margin-top:25px;'><h1>Semestre " + semestre + "<h1>\n\
                              <thead><tr><th>Materia</th><th>Calificacion</th><th>Evaluacion</th></tr></thead><tbody>";
                }
                var evaluacion = "";
                if (kardex[k].vecesCursada === 1) {
                    evaluacion = "Ordinario";
                } else if (kardex[k].vecesCursada === 2) {
                    evaluacion = "Repite";
                } else if (kardex[k].vecesCursada === 3) {
                    evaluacion = "Especial";
                }
                if (kardex[k].promedio >= 70) {
                    kardexHTML += "<tr><td>" + kardex[k].nombreMateria + "</td><td>" + kardex[k].promedio + "</td><td>" + evaluacion + "</td></tr>";
                    promedioPorSemestre.push(kardex[k].promedio);
                }
                if (contador === kardex.length) {
                    var promedio = promedioPorSemestre.reduce((a, b) => a + b, 0) / promedioPorSemestre.length;
                    kardexHTML += "<tr><td></td><td>Promedio:</td><td>" + promedio + "</td></tr></tbody></table>";
                }
                console.log(semestre);

                semestre === kardex[k].semestreCurso;
                contador++;
            }
            document.getElementById("showKardex").innerHTML = kardexHTML;
        }
    };
    xhttp.open("GET", url, true);
    xhttp.send();
}

