
var images = document.getElementsByName("carrouselImage");
var numberOfImages = images.length;
var activeImage = 0;
export var pauseCarrousel = false;
var cuadroDialogo = false;
var inputDelete = document.getElementById("numberImage");

export function initCarrousel() {

    if (inputDelete) {
        inputDelete.value = 0;
        inputDelete.onmouseover = onMouseOver;
        inputDelete.onmouseout = onMouseOut;
    }
    for (var i = 0; i < numberOfImages; i++) {
        if (i === 0) {
            images[i].className = "activeimgCarrousel";
        } else {
            images[i].className = "disableimgCarrousel";
        }
        document.getElementById("buttonsCarrousel").appendChild(createButton(i));
    }
    document.getElementById("carrousel").onmouseover = onMouseOver;
    document.getElementById("carrousel").onmouseout = onMouseOut;
    if (document.getElementById("deleteFotoCarrousel")) {
        document.getElementById("deleteFotoCarrousel").onclick = function () {
            cuadroDialogo = true;
            deleteImageCarrousel(event);
        };
    }
    var interval = setInterval(intervalCarrousel, 3000);
}

function onMouseOver() {
    pauseCarrousel = true;
}

function onMouseOut() {
    if (!cuadroDialogo) {
        pauseCarrousel = false;
    }
}

function createButton(numberImage) {
    var button = document.createElement("div");
    button.onclick = function () {
        buttonClick(numberImage);
    };
    return button;
}

function buttonClick(numberImage) {
    activeImage = numberImage;
    if (inputDelete) {
        inputDelete.value = activeImage;
    }
    for (var i = 0; i < numberOfImages; i++) {
        if (i !== activeImage) {
            images[i].className = "disableimgCarrousel";
        } else {
            images[i].className = "activeimgCarrousel";
        }
    }
}

function intervalCarrousel() {
    if (!pauseCarrousel) {
        activeImage++;
        if (activeImage >= numberOfImages) {
            activeImage = 0;
        }
        if (inputDelete) {
            inputDelete.value = activeImage;
        }
        for (var j = 0; j < numberOfImages; j++) {
            if (j !== activeImage) {
                images[j].className = "disableimgCarrousel";
            } else {
                images[j].className = "activeimgCarrousel";
            }
        }
    }
}

export function deleteImageCarrousel(e) {
    pauseCarrousel = true;
    Swal.fire({
        title: 'Eliminar imagen',
        text: "No podras revertir este cambio",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.value) {
            document.getElementById("deleteFotoCarrousel").submit();
            cuadroDialogo = false;
        } else {
            pauseCarrousel = false;
            cuadroDialogo = false;
        }
    });
}
;

