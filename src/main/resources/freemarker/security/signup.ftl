<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >

<#import "/spring.ftl" as spring/>

<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] />

<head>
	<link type="image/x-icon" href="/images/favicon.ico" rel="shortcut icon">
	<link type="image/x-icon" href="/images/favicon.ico" rel="icon">

    <title>SignUp</title>
    
	<!-- jQuery package -->
    <script type="text/javascript" src="/webjars/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/jquery-validation/1.13.0/jquery.validate.min.js"></script>
	<!-- Bootstrap package -->
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.2/css/bootstrap.min.css'>
	
	<link rel='stylesheet' href='/css/iservport.css'>

	<link rel='stylesheet' href='/css/signup.css'>

    <script type="text/javascript" src="/webjars/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    
    <link rel='stylesheet' href="/webjars/font-awesome/4.3.0/css/font-awesome.css">
   
</head>
<body class="gray-bg">

	<div id="unauth">
	<div id="main" class="container" data-ng-controller="SecurityController as securityCtrl"  class="middle-box text-center loginscreen  animated fadeInDown">
	<div class="container-small" data-ng-cloak >

	<!--
	 ! Logo
	 !-->
	<div class="clearfix text-center">
		<img src="/images/logo.png" alt="">
	</div>

	<h3 align="middle">Register for free.</h3>
	<p align="middle">Enter with your data to create a new account.</p>
	<br/>

    <div class="panel panel-default">
    <div class="panel-heading">
    	<h3 class="panel-title"><span class="glyphicon glyphicon-user"></span> Contact data</h3>
    </div>
	<div class="panel-body">
		<form class="m-t" role="form"  method="POST" name="form"  id="signup" action="/signup/">
		
			<#--
			 # CSRF
			 #-->
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

			<#--
			 # Context id
			 #-->
			<input type="hidden" name="contextId" value="${contextId!1}" />
				
			<#--
			 # Invalid e-mail
			 #-->
			<#if emailError?? && emailError="invalid" ><div class="alert alert-danger" role="alert">
				<@spring.message "label.email.invalid" /> </div>
			</#if>

			<#if email??>cf
				<type="email" class="form-control" placeholder="Email" required="true" id="email">
			<#else> 
				<div id="form-group-email" class="form-group">
					<div class="input-group">
						<input type="email" name="principal" required="true" id="email" 
							data-ng-model="principal" data-ng-keyUp="emailTester()"
							data-ng-blur="emailTester();saveEmail(principal)"
							value="${principal!''}"
							placeholder="E-mail" class="form-control">
									
						  <span class="input-group-addon" data-ng-show="emailOk">
						  <i class="fa fa-check" style="color:green;" ></i>
						  </span>
				  		  <span class="input-group-addon" data-ng-show="!emailOk">
						  <i class="fa fa-close" style="color:red;" ></i>
						  </span>
					  </div>
				</div>
				<div class="alert alert-danger alert-dismissible" role="alert" data-ng-show="(!userNotExists || !emailOk) && showAlerts" >
				  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				  <div data-ng-show="!userNotExists">
				  	<strong>Negado!</strong> Este email já está cadastrado.
				  </div> 	
				  <div data-ng-show="!emailOk" data-ng-hide="!userNotExists">
				  	<strong>Negado!</strong> Email Inválido.
				  </div> 
				</div>	
	
			</#if>
			
			
			<div class="form-group">
			<input  type="text" class="form-control" name="firstName"  placeholder="Name" required="" />
			
			<#--
			 # Usuário já existe
			 #-->
			<#if userExists??><div class="alert alert-danger" role="alert"><@spring.message "label.user.exists" /> </div></#if>
			</div>

			<div id="input-group-lastName" class="form-group">
				<input type="text" required="true" name="lastName" id="lastName"  placeholder="LastName" class="form-control">
			</div>
			
			<div id="input-group-domain" class="form-group">
				<input type="text" required="true" name="domain" id="domain"  placeholder="Domain" class="form-control">
			</div>

			<div class="row">
				<div class="col-md-8">
					<p><input type="checkbox" name="licenseAccepted" data-ng-model="checked"> Read and accepted the term of use.</p>
				</div>
				<div class="col-md-4">
					<a target="_new" class="pull-right" data-ng-href="/signup/license/" href="/signup/license/">See terms of use.</a>
				</div>
			</div>
			
			<div id="input-group-submit" class="form-group">
				<button type="submit" class="form-control btn btn-primary full-width" data-ng-disabled="!emailOk || !checked"> Send confirmation e-mail. </button>
			</div>
		</form>
		</div>
		<div class="panel-footer">
			<div class="row">
				<div class="col-md-6"> Already have an account?</div>
				<div class="col-md-6">	<a class="pull-right" href="/login">Login</a></div>
			</div>
		</div>
	</div>

<footer class="footer">
		<hr>
        <p>© I-SERV Enterprise Consulting Ltda. 2015</p>
  
  		<div class="row">
    	<div class="col-md-12">
	 		<small> 
		 			<a class="text-muted" target="_new" data-ng-href="/signup/privacy/" href="/signup/privacy/"> Privacy Policy</a>  |
		    		<a target="_new" class="text-muted" data-ng-href="/signup/license/" href="/signup/license/"> Terms of Use.</a>
			</small>
     	</div>
		</div>
</footer>
	
			

	</div><!-- .container-small -->
	</div><!-- #main --> 
	</div><!-- #unauth -->

	
	<!-- AngularJs package -->
	
	<!-- AngularJs package -->
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular.min.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular-sanitize.min.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular-resource.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular-route.min.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular-cookies.min.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/angular-animate.min.js"></script>
	<script type="text/javascript" src="/webjars/angularjs/1.4.3/i18n/angular-locale_${locale_!'pt-br'}.js"></script>
	<script type="text/javascript" src="/webjars/angular-loading-bar/0.7.1/loading-bar.min.js"></script>
	<!-- <script type="text/javascript" src="/webjars/angular-ui-bootstrap/0.13.1/ui-bootstrap-tpls.js"></script>
	 -->
	<script src="/js/ui-bootstrap-tpls-0.13.2.min.js"></script>
	<script type="text/javascript" src="/webjars/angular-ui-utils/0.2.2/ui-utils.min.js"></script>
	<!-- Services to apps-->
	<script type="text/javascript" src="/assets/iservport-layout-std.js"></script>
	<script type="text/javascript" src="/assets/services.js"></script>
	<script type="text/javascript" src="/assets/security/ng-security-module.js"></script>

</body>
</html>
