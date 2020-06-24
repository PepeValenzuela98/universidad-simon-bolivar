
export function initValidations() {
    secretarias();
    alumnos();
    docentes();
    telefonoFill();
    grupos();
    calificacionesParciales();
    generarBoleta();
    generarJustificante();
    if (document.getElementsByClassName("buttonEntitydelete")) {
        var entityDeleteButtons = document.getElementsByClassName("buttonEntitydelete");
        Array.from(entityDeleteButtons).forEach(function (element) {
            element.addEventListener('click', function (event) {
                event.preventDefault();
                Swal.fire({
                    title: 'Eliminar',
                    text: "Se eliminara todo lo relacionado ¿Seguro deseas realizar esta accion?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si',
                    cancelButtonText: 'Cancelar'
                }).then((result) => {
                    if (!result.value) {
                        return false;
                    } else {
                        window.location.href = element.href;
                    }
                });
            });
        });
    }
}


function secretarias() {
    if (document.getElementById("submitFormSecretaria")) {
        var numeroDeControlElement = document.getElementById("numeroDeControl");
        var numeroDeControl = numeroDeControlElement.value;
        if (document.getElementById("contraseña")) {
            var contraseñaElement = document.getElementById("contraseña");
            var contraseña = contraseñaElement.value;
        }
        var carreraElement = document.getElementById("carrera");
        var carrera = carreraElement.value;

        document.getElementById("submitFormSecretaria").addEventListener('click', function () {
            if (fieldValidationsSecretaria()) {
                Swal.fire({
                    title: 'Realizar cambios',
                    text: "¿Seguro deseas realizar esta accion?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si',
                    cancelButtonText: 'Cancelar'
                }).then((result) => {
                    if (result.value) {
                        numeroDeControlElement.value = numeroDeControl;
                        if (document.getElementById("contraseña")) {
                            contraseñaElement.value = contraseña;
                        }
                        carreraElement.value = carrera;
                        document.getElementById("formSecretaria").submit();

                    }
                });
            }
        });
    }
}

function fieldValidationsSecretaria() {
    var validate = true;
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("nombre").value)) {
        var spanError = document.getElementById("nombreError");
        spanError.className = "errorField";
        spanError.textContent = "Nombre no valido";
        validate = false;
    }
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("apellido").value)) {
        var spanError = document.getElementById("apellidoError");
        spanError.className = "errorField";
        spanError.textContent = "Apellido no valido";
        validate = false;
    }
    if (!/^\d{3}-\d{3}-\d{4}$/.test(document.getElementById("telefono").value)) {
        var spanError = document.getElementById("telefonoError");
        spanError.className = "errorField";
        spanError.textContent = "Numero de telefono no valido";
        validate = false;
    }
    if (!/^[\w_-]+(@){1}[a-z]+\.{1}[a-z]{2,3}$/.test(document.getElementById("correoElectronico").value)) {
        var spanError = document.getElementById("correoElectronicoError");
        spanError.className = "errorField";
        spanError.textContent = "Correo electronico no valido";
        validate = false;
    }
    return validate;
}

function alumnos() {
    if (document.getElementById("submitFormAlumno")) {
        var numeroDeControlElement = document.getElementById("numeroDeControl");
        var numeroDeControl = numeroDeControlElement.value;
        if (document.getElementById("contraseña")) {
            var contraseñaElement = document.getElementById("contraseña");
            var contraseña = contraseñaElement.value;
        }

        var carreraElement = document.getElementById("carrera");
        var carrera = carreraElement.value;
        document.getElementById("submitFormAlumno").addEventListener('click', function () {
            if (fieldValidationsAlumno()) {
                numeroDeControlElement.value = numeroDeControl;
                if (document.getElementById("contraseña")) {
                    contraseñaElement.value = contraseña;
                }
                carreraElement.value = carrera;
                document.getElementById("formAlumno").submit();
            }
        });
    }

}

function fieldValidationsAlumno() {
    var validate = true;
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("nombre").value)) {
        var spanError = document.getElementById("nombreError");
        spanError.className = "errorField";
        spanError.textContent = "Nombre no valido";
        validate = false;
    }
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("apellido").value)) {
        var spanError = document.getElementById("apellidoError");
        spanError.className = "errorField";
        spanError.textContent = "Apellido no valido";
        validate = false;
    }
    if (!/^\d{3}-\d{3}-\d{4}$/.test(document.getElementById("telefono").value)) {
        var spanError = document.getElementById("telefonoError");
        spanError.className = "errorField";
        spanError.textContent = "Numero de telefono no valido";
        validate = false;
    }
    if (!/^[\w_-]+(@){1}[a-z]+\.{1}[a-z]{2,3}$/.test(document.getElementById("correoElectronico").value)) {
        var spanError = document.getElementById("correoElectronicoError");
        spanError.className = "errorField";
        spanError.textContent = "Correo electronico no valido";
        validate = false;
    }
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("nombreEmergencia").value)) {
        var spanError = document.getElementById("nombreEmergenciaError");
        spanError.className = "errorField";
        spanError.textContent = "Nombre no valido";
        validate = false;
    }
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("apellidoEmergencia").value)) {
        var spanError = document.getElementById("apellidoEmergenciaError");
        spanError.className = "errorField";
        spanError.textContent = "Apellido no valido";
        validate = false;
    }
    if (!/^\d{3}-\d{3}-\d{4}$/.test(document.getElementById("telefonoEmergencia").value)) {
        var spanError = document.getElementById("telefonoEmergenciaError");
        spanError.className = "errorField";
        spanError.textContent = "Numero de telefono no valido";
        validate = false;
    }
    return validate;
}

function docentes() {
    if (document.getElementById("submitFormDocente")) {
        var numeroDeControlElement = document.getElementById("numeroDeControl");
        var numeroDeControl = numeroDeControlElement.value;
        if (document.getElementById("contraseña")) {
            var contraseñaElement = document.getElementById("contraseña");
            var contraseña = contraseñaElement.value;
        }
        document.getElementById("submitFormDocente").addEventListener('click', function () {
            if (fieldValidationsDocente()) {
                numeroDeControlElement.value = numeroDeControl;
                if (document.getElementById("contraseña")) {
                    contraseñaElement.value = contraseña;
                }
                document.getElementById("formDocente").submit();
            }
        });
    }
}

function fieldValidationsDocente() {
    var validate = true;
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("nombre").value)) {
        var spanError = document.getElementById("nombreError");
        spanError.className = "errorField";
        spanError.textContent = "Nombre no valido";
        validate = false;
    }
    if (!/^[A-Za-z\s]+$/.test(document.getElementById("apellido").value)) {
        var spanError = document.getElementById("apellidoError");
        spanError.className = "errorField";
        spanError.textContent = "Apellido no valido";
        validate = false;
    }
    if (!/^\d{3}-\d{3}-\d{4}$/.test(document.getElementById("telefono").value)) {
        var spanError = document.getElementById("telefonoError");
        spanError.className = "errorField";
        spanError.textContent = "Numero de telefono no valido";
        validate = false;
    }
    if (!/^[\w_-]+(@){1}[a-z]+\.{1}[a-z]{2,3}$/.test(document.getElementById("correoElectronico").value)) {
        var spanError = document.getElementById("correoElectronicoError");
        spanError.className = "errorField";
        spanError.textContent = "Correo electronico no valido";
        validate = false;
    }
    return validate;
}

function telefonoFill() {
    if (document.getElementById("telefono")) {
        var telefono = document.getElementById("telefono");
        telefono.addEventListener("keypress", () => {
            if (telefono.value.length === 3 | telefono.value.length === 7) {
                telefono.value += "-";
            }

        });
    }
    if (document.getElementById("telefonoEmergencia")) {
        var telefonoEmergencia = document.getElementById("telefonoEmergencia");
        telefonoEmergencia.addEventListener("keypress", () => {
            if (telefonoEmergencia.value.length === 3 | telefonoEmergencia.value.length === 7) {
                telefonoEmergencia.value += "-";
            }

        });
    }
}

function grupos() {
    if (document.getElementById("grupoForm")) {
        document.getElementById("saveGrupoHorario").addEventListener("click", function () {

            document.getElementById("grupoForm").submit();
        });
    }
}

function calificacionesParciales() {
    if (document.getElementsByClassName("calificacion")) {
        if (document.getElementById("saveCalificaciones")) {
            document.getElementById("saveCalificaciones").addEventListener("click", function () {
                var inputCalificion = document.getElementsByClassName("calificacion");
                var error = false;
                Array.from(inputCalificion).forEach(function (element) {
                    if (element.value < 0 || element.value > 100) {
                        error = true;
                    }
                });
                if (!error) {
                    document.getElementById("errorField").innerHTML = "";
                    document.getElementById("formCalificaciones").submit();

                } else {
                    document.getElementById("errorField").innerHTML = "Una o varias calificaciones no son validas compruebe que esten en un rango de 0 y 100";
                }
            });
        }
    }
}

function generarBoleta() {
    if (document.getElementById("generarPDFInd")) {
        document.getElementById("generarPDFInd").addEventListener("click", function (e) {
            var numeroDeControl = document.getElementById("numeroDeControlAlumno");
            if (numeroDeControl.value) {
                var link = document.getElementById("generarPDFInd");
                link.href = link.href.replace("$", numeroDeControl.value);
            } else {
                e.preventDefault();
                document.getElementById("errorField").innerHTML = "El numero de control no puede estar vacio";
            }
        });
    }
}

function generarJustificante() {
    if (document.getElementById("justificanteForm")) {
        var fecha = new Date();
        document.getElementById("fechaInicio").value = fecha.getFullYear() + "-" + pad(fecha.getMonth() + 1, 2) + "-" + pad(fecha.getDate(), 2);
        document.getElementById("fechaFin").value = fecha.getFullYear() + "-" + pad(fecha.getMonth() + 1, 2) + "-" + pad(fecha.getDate(), 2);
        document.getElementById("saveJustificante").addEventListener("click", function (e) {


            if (!fieldValidationJustificante()) {
                document.getElementById("justificanteForm").submit();
            }

        });
    }
}

function fieldValidationJustificante() {
    var numeroDeControl = document.getElementById("numeroDeControlAlumno");
    var fechaInicio = document.getElementById("fechaInicio");
    var fechaFin = document.getElementById("fechaFin");
    var errors = false;

    if (!numeroDeControl.value) {
        document.getElementById("numeroDeControlError").innerHTML = "El numero de control no puede estar vacio";
        errors = true;
    }

    var fechaInicioArray = fechaInicio.value.split("-");
    var fechaFinArray = fechaFin.value.split("-");

    if (fechaInicioArray[0] <= fechaFinArray[0]) {
        if (fechaInicioArray[1] <= fechaFinArray[1]) {
            if (fechaInicioArray[2] <= fechaFinArray[2]) {
                errors = false;
            } else {
                errors = true;
                document.getElementById("fechaValidaError").innerHTML = "Compruebe que la fecha de inicio sea menor a la de fin";
            }
        } else {
            errors = true;
            document.getElementById("fechaValidaError").innerHTML = "Compruebe que la fecha de inicio sea menor a la de fin";
        }
    } else {
        errors = true;
        document.getElementById("fechaValidaError").innerHTML = "Compruebe que la fecha de inicio sea menor a la de fin";
    }
    return errors;
}

function pad(n, width, z) {
    z = z || '0';
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
}