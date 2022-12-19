package com.isacore.sgc.acta.service.impl;

import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.quality.exception.UsuarioErrorException;
import com.isacore.quality.model.Area;
import com.isacore.quality.repository.IEmployeeRepo;
import com.isacore.quality.service.impl.GeneradorContrasena;
import com.isacore.sgc.acta.model.Employee;
import com.isacore.sgc.acta.model.Role;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IRoleRepo;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.sgc.acta.service.IUserImptekService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserImptekServiceImpl implements IUserImptekService {

    private static final Log LOG = LogFactory.getLog(UserImptekServiceImpl.class);

    private IUserImptekRepo repo;
    private IRoleRepo repoRole;
    private IEmployeeRepo employeeRepo;
    private GeneradorContrasena generadorContrasena;

    @Autowired
    public UserImptekServiceImpl(IUserImptekRepo repo, IRoleRepo repoRole, IEmployeeRepo employeeRepo,
                                 GeneradorContrasena generadorContrasena) {
        this.repo = repo;
        this.repoRole = repoRole;
        this.employeeRepo = employeeRepo;
        this.generadorContrasena = generadorContrasena;
    }

    @Override
    public List<UserImptek> findAll() {
        return this.repo.findAll();
    }

    @Transactional
    @Override
    public UserImptek create(UserImptek user) {
        this.validarUnicoUsuario(user.getIdUser());
        Employee empleado = this.crearEmpleado(user.getEmployee());
        UserImptek userNuevo = new UserImptek(user.getIdUser(), empleado, user.getRole());
        userNuevo.setUserPass(this.generadorContrasena.generar(user.getEmployee().getCiEmployee()));
        LOG.info(String.format("Usuario registrado %s", userNuevo));
        return this.repo.save(userNuevo);
    }

    private Employee crearEmpleado(Employee dto) {
        this.validarUnicoEmpleado(dto.getCiEmployee());
        Employee empleado = new Employee(
            dto.getCiEmployee(),
            dto.getKind(),
            dto.getArea(),
            dto.getName(),
            dto.getLastName(),
            dto.getEmail(),
            dto.getJob()
        );
        LOG.info(String.format("Empleado registrado %s ", empleado));
        return this.employeeRepo.save(empleado);
    }

    @Override
    public UserImptek findById(UserImptek u) {
        Optional<UserImptek> o = this.repo.findById(u.getIdUser());
        if (o.isPresent())
            return o.get();
        else
            return null;
    }

    @Transactional
    @Override
    public UserImptek update(UserImptek user) {
        UserImptek usuarioRecargado = this.buscarUsuario(user.getIdUser());
        usuarioRecargado.setRole(user.getRole());
        usuarioRecargado.getEmployee().setCiEmployee(user.getEmployee().getCiEmployee());
        usuarioRecargado.getEmployee().setName(user.getEmployee().getName());
        usuarioRecargado.getEmployee().setLastName(user.getEmployee().getLastName());
        usuarioRecargado.getEmployee().setArea(user.getEmployee().getArea());
        usuarioRecargado.getEmployee().setKind(user.getEmployee().getKind());
        usuarioRecargado.getEmployee().setState(user.getEmployee().getState());
        usuarioRecargado.getEmployee().setEmail(user.getEmployee().getEmail());
        LOG.info(String.format("Usuario actualizado %s", usuarioRecargado));
        return usuarioRecargado;
    }

    @Override
    public boolean delete(String id) {
        return true;

    }

    @Override
    public UserImptek findByUserImptek(String nickname) {

        List<Object[]> rowSql = this.repo.findUserByNickname(nickname);

        if (!(rowSql.isEmpty() || rowSql == null)) {
            Object[] o = rowSql.get(0);

            UserImptek ui = new UserImptek();
            ui.setIdUser((String) o[0]);
            LocalDateTimeConverter ldtc = new LocalDateTimeConverter();
            ui.setLastAccess(ldtc.convertToEntityAttribute((Timestamp) o[1]));
            ui.setLastKeyDateChange(ldtc.convertToEntityAttribute((Timestamp) o[2]));
            ui.setNickName((String) o[3]);

            Employee emp = new Employee();
            emp.setCiEmployee((String) o[4]);

            Optional<Role> op = this.repoRole.findById((String) o[5]);
            if (op.isPresent())
                ui.setRole(op.get());

            //ui.setRole(this.repoRole.findOne((String)o[6]));

            emp.setName((String) o[7]);
            emp.setLastName((String) o[8]);
            emp.setJob((String) o[9]);
            emp.setState((Boolean) o[10]);
            emp.setEmail((String) o[11]);
            Area a = new Area();

            a.setNameArea((String) o[12]);
            emp.setArea(a);
            ui.setEmployee(emp);

            return ui;

        }

        return null;
    }

    @Override
    public UserImptek findOnlyUserByNickname(String nickname) {
        return this.repo.findOnlyUserByNickname(nickname);
    }

    @Transactional(readOnly = true)
    @Override
    public UserImptek findByUserId(String idUser) {
        Optional<UserImptek> usuarioRecargado = this.repo.findByIdUser(idUser);
        return usuarioRecargado.orElseGet(UserImptek::new);
    }

    @Transactional
    @Override
    public boolean reestablecerContrasena(UserImptek userImptek) {
        UserImptek usuario = this.buscarUsuario(userImptek.getIdUser());
        usuario.setUserPass(this.generadorContrasena.generar(userImptek.getUserPass()));
        LOG.info(String.format("Usuario %s contrasena reestablecida", usuario.getIdUser()));
        return true;
    }

    @Override
    public List<UserImptek> listarActivos() {
        return this.repo.findByEmployeeStateTrue();
    }

    private void validarUnicoUsuario(String usuario) {
        Optional<UserImptek> usuarioOP = this.repo.findByIdUser(usuario);
        if (usuarioOP.isPresent())
            throw new UsuarioErrorException(String.format("Usuario %s ya existe", usuarioOP.get().getIdUser()));
    }

    private void validarUnicoEmpleado(String identificacion) {
        Optional<Employee> empleadoOP = this.employeeRepo.findByCiEmployee(identificacion);
        if (empleadoOP.isPresent())
            throw new UsuarioErrorException(String.format("Empleado %s ya existe", empleadoOP.get().getCiEmployee()));
    }

    private UserImptek buscarUsuario(String idUser) {
        Optional<UserImptek> usuarioOP = this.repo.findByIdUser(idUser);
        if (!usuarioOP.isPresent())
            throw new UsuarioErrorException(String.format("Usuario %s no encontrado", idUser));
        return usuarioOP.get();
    }

}
