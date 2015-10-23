angular.module('app.services').
value('lang', {
	NO_CONTENT:'Não há conteúdo',
	SAVE:'Criar usuário',
	CLOSE:'Fechar',
	ONE_INSTRUMENT:'Conteúdo',
	MANY_INSTRUMENT:'@{}@ conteúdos',
	STAFF_MEMBER_NOT_FOUND : 'Membro da Equipe não foi encontrado' , 
	STAFF_MEMBER_CANNOT_DELETE : 'Membro não pode ser deletado.',
	//pasta
	CONTENT_FOLDER : 'Pasta' ,
	CONTENT_BY: 'Documentos de ',
	NO_CONTENT : '',
	NO_CONTENT_MSG :'<strong>Não há documentos!</strong> clique no botão abaixo para cadastrar um Documento',
	
	NEW_PERSON:'Nova pessoa',
	PERSONAL_DATA_FOR:'Dados pessoais para ',
	USER_DATA_FOR:'Dados do usuário para ',
	SEARCH:'Procurar e-mail',
	SEARCH_ALERT:'Antes de cadastrar um novo usuário, é preciso verificar se o e-mail já foi utilizado previamente.',
	SEARCH_ALERT_EXISTS:'Usuário existente!',
	
	//geral
	ISSUE_DATE:'Incluído em',
	NEXT_CHECK_DATE:'Próxima verificação',
	INTERNAL_NUMBER:"Número" ,
	COMPLETE:"Progresso" ,
	OWNER:"Responsável" ,
	
	SAVE_CONTENT_PUBLISH:'Confirmar publicação',	
	CONTENT_PUBLISH:'Publicação de Documentos',
	PUBLISH:'Publicar',
	
	IDENTITY_TYPE:'O e-mail é',
	IDENTITY_TYPE_NOT_ADDRESSABLE:'Somente para identificação',
	IDENTITY_TYPE_ORGANIZATIONAL_EMAIL:'Fornecido pela organização',
	IDENTITY_TYPE_PERSONAL_EMAIL:'Pessoal',
//	gender
	IDENTITY_GENDER:'Gênero',
	IDENTITY_GENDER_MALE:'Masculino',
	IDENTITY_GENDER_FEMALE:'Feminino',
	IDENTITY_GENDER_NOT_SUPPLIED:'Não informado',
	
	DISPLAY_NAME:'Nome mais usado',
	FIRST_NAME:'Primeiro nome',
	LAST_NAME:'Sobrenome',
	USER_BIRTH_DATE:'Nascimento',
	KNOWLEDGE_NOT_UNIQUE:'Já existe treinamento para este usuário e/ou Documento cadastrado'
	, PHASE:'Fase'
	, PHASES: 'Fases',	
	// Review
	DETAIL:'Detalhe',
	_getLocalizationKeys: function() {
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});
