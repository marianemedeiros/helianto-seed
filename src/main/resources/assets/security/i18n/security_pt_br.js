angular.module('app.services').
value('lang', {
	//geral
	ISSUE_DATE:'Incluído em',
	NEXT_CHECK_DATE:'Próxima verificação',
	INTERNAL_NUMBER:"Número" ,
	COMPLETE:"Progresso" ,
	OWNER:"Responsável" ,
	
	_getLocalizationKeys: function() {
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});
