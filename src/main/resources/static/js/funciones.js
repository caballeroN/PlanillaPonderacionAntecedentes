//Redirigir a página para crear profesor

document.addEventListener("DOMContentLoaded", function () {
  // Verificar si el botón existe antes de intentar agregar el event listener
  const crearProfesorButton = document.getElementById("crearProfesor");
  if (crearProfesorButton) {
    crearProfesorButton.addEventListener("click", function (event) {
      // Evitar que el enlace abra la página directamente
      event.preventDefault();

      // Valor específico que se quiere enviar en la solicitud POST
      var dniValue = 0;
      var url = "/buscarxdni";

      // Formulario oculto dinámicamente
      var form = document.createElement("form");
      form.action = url;
      form.method = "POST";

      // Input para el valor de DNI
      var dniInput = document.createElement("input");
      dniInput.type = "number";
      dniInput.name = "dni";
      dniInput.value = dniValue; // Asignar el valor específico
      form.appendChild(dniInput);

      // Botón submit y agregarlo al formulario
      var submitButton = document.createElement("button");
      submitButton.type = "submit";
      submitButton.innerText = "Buscar";
      form.appendChild(submitButton);

      // Agregar el formulario al cuerpo del documento y enviarlo automáticamente
      document.body.appendChild(form);
      form.submit();
    });
  }
});

//Bloquear busqueda con campos vacíos
document.addEventListener("DOMContentLoaded", function () {
	
    const campoBusquedaPorID = document.getElementById("campoBusquedaPorID");
    const botonBuscarPorID = document.getElementById("botonBuscarPorID");

    const campoBusquedaPorDNI = document.getElementById("campoBusquedaPorDNI");
    const botonBuscarPorDNI = document.getElementById("botonBuscarPorDNI");

    function validarCampoYBoton(campo, boton) {
        const valorCampoBusqueda = campo.value.trim();
        boton.disabled = !valorCampoBusqueda;
    }

    // Añadir evento input a cada campo de búsqueda
    campoBusquedaPorID.addEventListener("input", function () {
        validarCampoYBoton(campoBusquedaPorID, botonBuscarPorID);
    });

    campoBusquedaPorDNI.addEventListener("input", function () {
        validarCampoYBoton(campoBusquedaPorDNI, botonBuscarPorDNI);
    });

    // Llamar a la función inicialmente para configurar el estado inicial
    validarCampoYBoton(campoBusquedaPorID, botonBuscarPorID);
    validarCampoYBoton(campoBusquedaPorDNI, botonBuscarPorDNI);
});



//Detectar al menos un checkbox seleccionado al editar categorías

document.addEventListener("DOMContentLoaded", function () {
    // Verifica si estamos en la página "/categorias_p"
    const isCategoriasPage = window.location.pathname.includes("/categorias_t");

    if (isCategoriasPage) {
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        const continuarButton = document.querySelector('button.ok');

        // Agrega un event listener al formulario para prevenir su envío si no hay checkbox seleccionados
        const form = document.querySelector("form");
        form.addEventListener("submit", function (event) {
            if (!alMenosUnCheckboxSeleccionado()) {
                event.preventDefault();
                alert("Selecciona al menos una categoría para continuar.");
            }
        });

        // Agrega un event listener a cada checkbox para verificar su estado y actualizar el botón "Continuar"
        checkboxes.forEach(function (checkbox) {
            checkbox.addEventListener("change", function () {
                actualizarBotonContinuar();
            });
        });

        // Función para verificar si al menos un checkbox está seleccionado
        function alMenosUnCheckboxSeleccionado() {
            return Array.from(checkboxes).some(function (checkbox) {
                return checkbox.checked;
            });
        }

        // Función para actualizar el estado del botón "Continuar"
        function actualizarBotonContinuar() {
            continuarButton.disabled = !alMenosUnCheckboxSeleccionado();
        }
    }
});