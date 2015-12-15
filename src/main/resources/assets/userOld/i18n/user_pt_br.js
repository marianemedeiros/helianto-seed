angular.module('app.services').
value('lang', {

	_CLOSE:'Fechar',
	_SAVE:'Atualizar',
	_EDIT: 'Editar',

	ALL:'Todos',
	ACTIVE: 'Ativo',
	ACTIVATE: 'Ativar',
	ASSOCIATE: 'Associar',

	CURRICULUM: 'Histórico',
	CHANGE_DATA: 'Mudar dados pessoais',
	CAREER:'Carreira',
	COMPETENCE_SPECIALIZATION_E_BACHELOR_DEGREE:'Graduação',
	COMPETENCE_SPECIALIZATION_E_BASIC:'Médio ou fundamental',
	COMPETENCE_SPECIALIZATION_E_DOCTORATE:'Doutorado',
	COMPETENCE_SPECIALIZATION_E_MASTER:'Mestrado (strictu sensu)',
	COMPETENCE_SPECIALIZATION_E_POST_DOCTORATE:'Pós-doutorado',
	COMPETENCE_SPECIALIZATION_E_SPECIALIZATION:'Pós graduação (latu sensu)',
	COMPETENCE_SPECIALIZATION_E_TECHNICAL_DEGREE:'Pós médio',
	COMPETENCE_SPECIALIZATION_S_SKILL:'Habilidade específica',
	COMPETENCE_SPECIALIZATION_T_APPROVAL:'Aprovação',
	COMPETENCE_SPECIALIZATION_T_CERTIFICTION:'Certificação',
	COMPETENCE_SPECIALIZATION_T_PARTICIPATION:'Participação',
	COMPETENCE_SPECIALIZATION_X_INDUSTRY:'Área de atuação',
	COMPETENCE_SPECIALIZATION_X_KNOWLEDGE:'Área de conhecimento',
	COMPETENCE_SPECIALIZATION_X_OPERATION:'Operação',
	COMPETENCE_UNIT_DAY:'Dia(s)',
	COMPETENCE_UNIT_EVENT:'Evento(s)',
	COMPETENCE_UNIT_HOUR:'Hora(s)',
	COMPETENCE_UNIT_MONTH:'Mes(es)',
	COMPETENCE_UNIT_NOT_APPLICABLE:'',
	COMPETENCE_UNIT_YEAR:'Ano(s)',
	CONSULTANCY:'Terceiros',
	
	DISPLAY_NAME:'Mostrar como',
	DISABLE: 'Desativar',

	EMAIL: 'E-mail',
	EVALUATION: 'Avaliação',
	
	FIRST_NAME:'Primeiro nome',
	FUNCTION:'Funções',

	GROUP:'Grupo',
	
	IDENTITY_GENDER_FEMALE:'Feminino',
	IDENTITY_GENDER_MALE:'Masculino',
	IDENTITY_GENDER_NOT_SUPPLIED:'Não informado',
	IDENTITY_GENDER:'Gênero',
	IDENTITY_TYPE_NOT_ADDRESSABLE:'Somente para identificação',
	IDENTITY_TYPE_ORGANIZATIONAL_EMAIL:'Fornecido pela organização',
	IDENTITY_TYPE_PERSONAL_EMAIL:'Pessoal',
	IDENTITY_TYPE:'O e-mail é',
	
	IMAGE_ASSOCIATE:'Associar imagem',
	IMAGE_CHOOSE:'Escolher Imagem',
	IMAGE_CHOOSE_ERR:'Imagem muito grande : máximo 2MB',
	IMAGE_CROP:'Corte a imagem para enviar',
	IMAGE_TO_SEND:'Imagem a ser enviada:',
	
	INACTIVE: 'Inativo',

	KNOWLEDGE_LEVEL_0:'Não requer',
	KNOWLEDGE_LEVEL_1:'Em treinamento',
	KNOWLEDGE_LEVEL_2:'Treinado, acompanhar',
	KNOWLEDGE_LEVEL_3:'Treinado',
	KNOWLEDGE_LEVEL_4:'Pode treinar',

	HIDE_INACTIVES: 'Oculdar inativos',
	LAST_NAME:'Sobrenome',
	
	MANY_USERS:'@{}@ pessoas',
	
	NEW_PASS: 'Nova Senha', 
	NEW_PERSON:'Nova pessoa',
	NEW_FUNCTION: 'Nova função',
	NEW_POST: 'Novo cargo',
	NEW_INFORMATION: 'Novo histórico',
	NEW_EVALUATION: 'Nova avaliação',
	NO_USER:'Não há pessoas',
	
	ONE_USER:'Pessoa',
	
	PEOPLE: 'Pessoas',
	PERSONAL_DATA: 'Dados pessoais',
	PASSWORD_TO_CHANGE: 'Nova Senha',
	PASSWORD_CONFIRMATION: 'Confirmação Nova Senha',

	SEARCH: 'Pesquisar',
	SEND: 'Enviar',
	SHOW_INACTIVES: 'Mostrar inativos',
	SYSTEM:'Sistema',
	
	UPLOAD_SUCCESS:'Upload bem sucedido!',

	USER_BIRTH_DATE:'Nascimento',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_0:'não requer',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_1:'Ensino fundamental, 1o. ano',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_10:'Ensino médio, 1a série',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_11:'Ensino médio, 2a série',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_12:'Ensino médio completo',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_13:'Ensino médio completo (4 anos)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_2:'Ensino fundamental, 2o. ano (1a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_3:'Ensino fundamental, 3o. ano (2a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_4:'Ensino fundamental, 4o. ano (3a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_5:'Ensino fundamental, 5o. ano (4a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_6:'Ensino fundamental, 6o. ano (5a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_7:'Ensino fundamental, 7o. ano (6a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_8:'Ensino fundamental, 8o. ano (7a. série)',
	USER_GROUP_MINIMUM_TIME_REQUIREMENT_9:'Ensino fundamental completo',
	USER_TYPE_A:'Todos',
	USER_TYPE_F:'Funções',
	USER_TYPE_J:'Carreira',
	USER_TYPE_S:'Sistema',
	USER_TYPE_Y:'Terceiros',
	USER_TYPE_G:'Administração',

	TRAINNING: 'Treinamento',
	_getLocalizationKeys: function() {
//		Returns an object that has as properties the same properties of this object.
//		The values of these properties is equal to the name of each properties.
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});
