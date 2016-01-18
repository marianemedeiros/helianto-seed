[#ftl]
<script >
	var email = [#if email??]'${email}' [#else]''[/#if];
</script>
[#include "../config/login-splash.html" /]
<div class="panel panel-default">

	<div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<h3 class="panel-title"><span class="glyphicon  glyphicon-lock"></span> Enter</h3>
			</div>
		</div>
	</div>
	
	<div class="panel-body">
	<form  name="form" data-ng-submit="login(username,password)" method="POST">
	
	
		[#--
		 # CSRF
		 #--]
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		
		[#--
		 # Context id
		 #--]
		<input type="hidden" name="contextId" value="${contextId!1}" />
			
		[#if error?? ]
			[#if user?? && !user.accountNonExpired  ]		
				<!--
				 ! Inactive user
				 !-->
				<div class="alert alert-danger" role="alert">
					<i class="fa fa-lock"></i> Inactive user. 
				</div>
			[#else]	
				<!--
				 ! Login error
				 !-->
				<div class="alert alert-danger" role="alert">
					<i class="fa fa-lock"></i> Login error.
				</div>		
			[/#if]
		[/#if]
		[#if userConfirmed?? ]
				<div class="alert alert-success" role="alert"><i class="fa fa-unlock"></i>Your username has been activated, do your login to enter.</div>		
		[/#if]
		[#if emailRecoverySent?? ]
				<div class="alert alert-warning" role="alert">Check your e-mail to recovery your password. </div>		
		[/#if]
		[#if emailRecoveryFailed?? ]
				<div class="alert alert-warning" role="alert">E-mail failed.</div>		
		[/#if]
		[#if recoverFail?? && recoverFail="true" ]<div class="alert alert-danger" role="alert">Failed to change password. </div>[/#if]
		
		[#if recoverFail?? && recoverFail="false"]<div class="alert alert-success" role="alert">Password successfully modified. </div>[/#if]

		<div id="form-group-email" class="form-group">
			<input type="text" required name="username" data-ng-model="username" placeholder="E-mail" class="form-control">
		</div>
		<div id="form-group-password" class="form-group">
			<input type="password" required name="password" data-ng-model="password" placeholder="Senha" class="form-control">
		</div>
		
		<!--
		 ! Forgot password
		 !-->
		<div class="row">
			<div class="col-md-6">
				<p><input type="checkbox" name="remember-me" > Remember Password</p>
			</div>
			<div class="col-md-6">
				<a target="_self" class="pull-right" data-ng-href="/recovery/">Forgot password?</a>
			</div>
		</div>
		
		<!--
		 ! Submit
		 !-->
		<hr/>
		<button type="submit" class="btn btn-primary" style="width: 100%" >Continue</button>
		
		
	</form>
	</div><!-- panel body -->
	<div class="panel-footer">
		<!--
		 ! New user
		 !-->
		<div class="row">
			<div class="col-md-6">
				<h5>Doesn't have an account yet?</h5>
			</div>
			<div class="col-md-6">
				<h5><a class="pull-right" href="/signup/">Create User Account</a></h5>
			</div>
		</div>
	</div><!-- panel footer -->
</div>
