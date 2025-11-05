// Se mantiene la función anónima para no contaminar el scope global
(function() {
    'use strict';

    const storageKey = 'theme-preference';

    // Función para obtener la preferencia guardada o la del sistema
    const getPreference = () => {
        const storedPreference = localStorage.getItem(storageKey);
        if (storedPreference) {
            return storedPreference;
        }
        // Si no hay nada guardado, usamos la preferencia del sistema operativo
        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    };

    // Función para aplicar el tema al HTML y al checkbox
    // Necesita los elementos como parámetros para funcionar
    const applyTheme = (theme, htmlElement, themeToggle) => {
        htmlElement.setAttribute('data-bs-theme', theme);
        if (themeToggle) {
            themeToggle.checked = (theme === 'dark');
        }
    };

    // --- LÓGICA PRINCIPAL ---
    // Esperamos a que toda la página HTML esté cargada antes de manipularla
    document.addEventListener('DOMContentLoaded', () => {
        
        // 1. Seleccionamos los elementos AHORA, cuando sabemos que existen
        const themeToggle = document.getElementById('theme-toggle');
        const htmlElement = document.documentElement;

        // Si por alguna razón no encontramos el botón en esta página, no hacemos nada más
        if (!themeToggle) {
            // Solo aplicamos el tema inicial y listo
            applyTheme(getPreference(), htmlElement, null);
            return;
        }

        // 2. Añadimos el listener para cuando el usuario haga clic
        themeToggle.addEventListener('change', () => {
            const newTheme = themeToggle.checked ? 'dark' : 'light';
            // Aplicamos el nuevo tema
            applyTheme(newTheme, htmlElement, themeToggle);
            // Guardamos la preferencia para futuras visitas
            localStorage.setItem(storageKey, newTheme);
        });

        // 3. Aplicamos el tema correcto al cargar la página
        const initialTheme = getPreference();
        applyTheme(initialTheme, htmlElement, themeToggle);
    });

})();