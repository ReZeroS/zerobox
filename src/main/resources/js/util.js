function jsonToQueryString(params) {
    let paramsArray = Object.keys(params).filter((key, _) => {
      if (params.hasOwnProperty(key)) {
        let val = params[key];
        return !(val === null || typeof val === 'undefined')
      }
    }).map((key, _) => (key + '=' + params[key]));
    return queryString = paramsArray.length > 0 ? "?" + paramsArray.join("&") : "";
}
    
