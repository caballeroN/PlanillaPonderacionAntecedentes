
//Redirigir a página para crear profesor

document.getElementById("crearProfesor").addEventListener("click", function(event) {

    // Evitar que el enlace abra la página directamente
    event.preventDefault();

    // Valor específico que se quiere enviar en la solicitud POST
    var dniValue = 0;
    var url = "/buscarxdni";

    //Formulario oculto dinámicamente
    var form = document.createElement("form");
    form.action = url;
    form.method = "POST";

    //Input para el valor de DNI
    var dniInput = document.createElement("input");
    dniInput.type = "number";
    dniInput.name = "dni";
    dniInput.value = dniValue; // Asignar el valor específico
    form.appendChild(dniInput);

    //Botón submit y agregarlo al formulario
    var submitButton = document.createElement("button");
    submitButton.type = "submit";
    submitButton.innerText = "Buscar";
    form.appendChild(submitButton);

    // Agregar el formulario al cuerpo del documento y enviarlo automáticamente
    document.body.appendChild(form);
    form.submit();
});