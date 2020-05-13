jsonToQueryString(params) {
    const paramsArray = Object.keys(params)
      .filter(
        (key) => {
          if (Object.prototype.hasOwnProperty.call(params, key)) {
            const val = params[key];
            return !(val === null || typeof val === 'undefined');
          }
          return false;
    }).map((key) => (`${key}=${params[key]}`));
    return paramsArray.length > 0 ? `?${  paramsArray.join("&")}` : "";
},
