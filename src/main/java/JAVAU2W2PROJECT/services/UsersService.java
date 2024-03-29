package JAVAU2W2PROJECT.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import JAVAU2W2PROJECT.entities.Dispositivo;
import JAVAU2W2PROJECT.entities.User;
import JAVAU2W2PROJECT.exceptions.BadRequestException;
import JAVAU2W2PROJECT.payloads.UserRegistrationPayload;
import JAVAU2W2PROJECT.repository.DispositiviRepository;
import JAVAU2W2PROJECT.repository.UsersRepository;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private DispositiviRepository dispositiviRepo;

	public User create(UserRegistrationPayload u) {
		usersRepo.findByEmail(u.getEmail()).ifPresent(user -> {
			throw new BadRequestException("Email " + user.getEmail() + " already in use!");
		});
		User newUser = new User(u.getUsername(), u.getName(), u.getSurname(), u.getEmail(), u.getPassword());
		return usersRepo.save(newUser);
	}

	public Page<User> find(int page, int size, String sortBy) {
		if (size < 0)
			size = 10;
		if (size > 100)
			size = 100;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		return usersRepo.findAll(pageable);
	}

	public User findById(UUID id) throws NotFoundException {
		return usersRepo.findById(id).orElseThrow(() -> new NotFoundException());
	}

	public User findByEmail(String email) throws NotFoundException {
		return usersRepo.findByEmail(email).orElseThrow(() -> new NotFoundException());
	}

	public User findByIdAndUpdate(UUID id, UserRegistrationPayload u) throws NotFoundException {
		User found = this.findById(id);

		found.setId(id);
		found.setUsername(u.getUsername());
		found.setName(u.getName());
		found.setSurname(u.getSurname());
		found.setEmail(u.getEmail());

		return usersRepo.save(found);
	}

	public User findByIdAndUpdatePassword(UUID id, UserRegistrationPayload u) throws NotFoundException {
		User found = this.findById(id);
		found.setPassword(u.getPassword());

		return usersRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		usersRepo.delete(found);
	}

	public Dispositivo assignDispositivoToUser(UUID dispositivoId, UUID userId) throws NotFoundException {
		Dispositivo dispositivo = dispositiviRepo.findById(dispositivoId).orElseThrow(() -> new NotFoundException());
		User user = usersRepo.findById(userId).orElseThrow(() -> new NotFoundException());

		dispositivo.setUser(user);

		return dispositiviRepo.save(dispositivo);
	}

}