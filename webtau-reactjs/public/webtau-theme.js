(function () {
  var themeNameKey = 'webtauTheme';
  var darkThemeName = 'webtau-dark';
  var lightThemeName = 'webtau-light';

  var webtauTheme = {
    changeHandlers: [],
    addChangeHandler(handler) {
      this.changeHandlers.push(handler);
    },
    removeChangeHandler(handler) {
      var idx = this.changeHandlers.indexOf(handler);
      this.changeHandlers.splice(idx, 1);
    },
    set(name) {
      this.name = name;
      document.body.className = name;

      var idx = 0;
      var len = this.changeHandlers.length;
      for (; idx < len; idx++) {
        this.changeHandlers[idx](name);
      }
    },
    setExplicitly(name) {
      storeThemeName(name);
      this.set(name);
    },
    setExplicitlyIfNotSetAlready(name) {
      const themeName = getStoredThemeName();
      if (themeName) {
        return;
      }

      this.setExplicitly(name);
    },
    toggle() {
      this.setExplicitly(this.name === lightThemeName ? darkThemeName : lightThemeName);
    },
  };

  var mediaThemeName = setLightMatchMediaListenerAndGetThemeName();
  var themeName = getStoredThemeName() || mediaThemeName;
  webtauTheme.set(themeName);

  window.webtauTheme = webtauTheme;

  function getStoredThemeName() {
    return localStorage.getItem(themeNameKey);
  }

  function storeThemeName(name) {
    return localStorage.setItem(themeNameKey, name);
  }

  function setLightMatchMediaListenerAndGetThemeName() {
    if (!window.matchMedia) {
      return darkThemeName;
    }

    var lightQuery = window.matchMedia('(prefers-color-scheme: light)');
    lightQuery.addEventListener('change', function (e) {
      const newThemeName = e.matches ? lightThemeName : darkThemeName;
      webtauTheme.setExplicitly(newThemeName);
    });

    return lightQuery.matches ? lightThemeName : darkThemeName;
  }
})();
