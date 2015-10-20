[#ftl]
	[#--
	 # Logo
	 #--]
	<div class="clearfix text-center">
		<a href="#" class="text-center" target="_self"><img src="/images/logo.png" alt="" ></a>
	</div>

	[#--
	 # Título: Change Password
	 #--]
	<div class="heading" style="margin-bottom: 40px;">
		<h3 class="text-center">Change Password</h3>
	</div>

	<div class="heading">
		<h4 align="middle">Your e-mail is [#if email??]>${email}[/#if]!</h4>
	</div>

	[#if userExists??]<div class="alert alert-danger" role="alert"><p>E-mail already registered!" </p> </div>[/#if]
	
	[#if recoverFail?? && recoverFail="true" ]<div class="alert alert-danger" role="alert"><p>Failed to change email.</p> </div>[/#if]
	[#if recoverFail?? && recoverFail="false"]<div class="alert alert-success" role="alert"><p>Password successfully modified.</p> </div>[/#if]
	[#if recoverFailMsg?? ]<div class="alert alert-warning" role="alert"><@spring.message "${recoverFailMsg}" /> </div>[/#if]
			
    
    <div class="panel panel-default">
    <div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<h3 class="panel-title"><span class="glyphicon glyphicon-wrench"></span> Password</h3>
			</div>
		</div>
	</div>
	
	<div class="panel-body">
    
    	[#--
    	 # Can be an external user or a user linked to a section.
    	 #--]
    	 
    	<#assign changeAction="/recovery/submit" />
    	<@sec.authorize access="isAuthenticated()">
    		<#assign changeAction="/recovery/submit" />
    	</@sec.authorize>

    
		<form method="POST" name="form"  id="change" action="/recovery/submit">
				
			[#--
			 # CSRF
			 #--]
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			
			[#--
			 # E-mail field (hidden), password and confirmation
			 #--]
			<input type="hidden" name="email" id="email" value="[#if email??]${email}[/#if]" >
			<password-checker password="password" cpassword="cpassword" ></password-checker>	
			<div id="form-group-password" class="form-group">
				<input type="password"  required="" name="password"  id="password" data-ng-model="password" placeholder="Senha" class="form-control">
			</div>
			
			</br>
			
			<div id="form-group-passwordc" class="form-group">
				<input type="password"  required="" name="cpassword" id="cpassword" data-ng-model="cpassword" placeholder="Password confirmation" class="form-control">
			</div>
			
			</br>
			
			<button type="submit" class="btn btn-primary" style="width: 100%;" data-ng-disabled="cannotChangePassword"> Change Password</button>
		
		</form>
		</div>

		
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-6">
				<h5>Home Page</h5>

			</div>
			<div class="col-md-6">
				<h5><a class="pull-right" href="/app/home">Home</a></h5>
			</div>
		</div>

	</div>
	</div>
