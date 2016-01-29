angular.module('app.services').
value('lang', {
	_SAVE: 'Salvar',
	_CLOSE: 'Fechar',

	ASSOCIATION:'Associações',
	AUTHORIZED: 'Autorizar',
	
	ALIAS: 'Domínio',
	ALIAS_HINT: 'Ex.: yoursite.com/optional',
	CONSULTANCY:'Consultoria',
	CITY: 'Cidade',

	ENTITY: 'Entitade',
	ENTITY_NAME: 'Razão social',

	FRANCHISE:'Franquias',

	HEALTH:'Saúde ocupacional',

	NEW_ENTITY: 'Nova entidade',
	NO_CONTENT:'Não há conteúdo',
	NETWORK: 'Network',
	NO_ENTITIES: 'Não há entidades',
	SUMMARY: 'Resumo',

	SPORT:'Esportes',
	STATE: 'Estado',
	SITUATION: 'Situação',

	URL_IMAGE: 'Url da imagem',

	WARNNING: 'clique no botão abaixo para cadastrar uma nova',
	_getLocalizationKeys: function() {
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});