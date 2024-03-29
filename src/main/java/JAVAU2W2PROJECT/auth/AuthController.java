package JAVAU2W2PROJECT.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import JAVAU2W2PROJECT.auth.payloads.AuthenticationSuccessfullPayload;
import JAVAU2W2PROJECT.entities.User;
import JAVAU2W2PROJECT.exceptions.UnauthorizedException;
import JAVAU2W2PROJECT.payloads.UserLoginPayload;
import JAVAU2W2PROJECT.payloads.UserRegistrationPayload;
import JAVAU2W2PROJECT.services.UsersService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UsersService usersService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody @Validated UserRegistrationPayload body) {
		User createdUser = usersService.create(body);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationSuccessfullPayload> login(@RequestBody UserLoginPayload body)
			throws NotFoundException {

		// verifico che esiste l'email nel db
		// se esiste controllo che la password e l'utente combacino con l email inserita
		// se combaciano generare un token
		// se fallisce lanciare un eccezione credenziali non valide
		User user = usersService.findByEmail(body.getEmail());
		if (!body.getPassword().matches(user.getPassword()))
			throw new UnauthorizedException("Credenziali non valide");
		String token = JWTTools.createToken(user);

		return new ResponseEntity<>(new AuthenticationSuccessfullPayload(token), HttpStatus.OK);
	}

}