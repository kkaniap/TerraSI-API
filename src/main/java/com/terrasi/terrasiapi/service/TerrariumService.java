package com.terrasi.terrasiapi.service;

import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.exception.ForbiddenException;
import com.terrasi.terrasiapi.exception.NotFoundException;
import com.terrasi.terrasiapi.exception.UnauthorizedException;
import com.terrasi.terrasiapi.model.JwtModel;
import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.model.TerrariumSettings;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.repository.TerrariumRepository;
import com.terrasi.terrasiapi.repository.TerrariumSettingsRepository;
import com.terrasi.terrasiapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TerrariumService {

    private final UserRepository userRepository;
    private final TerrariumRepository terrariumRepository;
    private final TerrariumSettingsRepository terrariumSettingsRepository;

    public TerrariumService(UserRepository userRepository, TerrariumRepository terrariumRepository,
                            TerrariumSettingsRepository terrariumSettingsRepository) {
        this.userRepository = userRepository;
        this.terrariumRepository = terrariumRepository;
        this.terrariumSettingsRepository = terrariumSettingsRepository;
    }

    public Page<Terrarium> getTerrariumsByToken(String accessToken, Pageable page){
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        return user.map(value -> terrariumRepository.getAllByUserId(value.getId(), page)).orElse(null);
    }

    public Terrarium getTerrariumById(Long id, String accessToken) throws NotFoundException, ForbiddenException, UnauthorizedException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(user.isPresent()){
            if(terrarium.isPresent()){
                if(user.get().getId().equals(terrarium.get().getUser().getId())){
                    return terrarium.get();
                }else {
                    throw new ForbiddenException();
                }
            }else {
                throw new NotFoundException();
            }
        }
        throw new UnauthorizedException();
    }

    public boolean saveTerrariumSettings(Long id, String accessToken, TerrariumSettings settings) throws UnauthorizedException, NotFoundException, ForbiddenException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(user.isPresent()){
            if(terrarium.isPresent()){
                if(terrarium.get().getUser().getId().equals(user.get().getId())){
                    terrariumSettingsRepository.save(settings);
                    return true;
                }else {
                    throw new ForbiddenException();
                }
            }else {
                throw new NotFoundException();
            }
        }
        throw new UnauthorizedException();
    }

    public boolean updateTerrariumName(Long id, String name, String accessToken) throws UnauthorizedException, NotFoundException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        if(user.isPresent()){
            if(terrarium.isPresent()){
                if(terrarium.get().getUser().getId().equals(user.get().getId())){
                    terrarium.get().setName(name);
                    terrariumRepository.save(terrarium.get());
                    return true;
                }else {
                    throw new UnauthorizedException();
                }
            }else {
                throw new NotFoundException();
            }
        }
        throw new UnauthorizedException();
    }

    public void sendTerrariumSettings(TerrariumSettings terrariumSettings, String accessToken){
        Optional<Terrarium> terrarium = terrariumRepository.getByTerrariumSettings(terrariumSettings.getId());
        if(terrarium.isPresent()){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<TerrariumSettings> body = new HttpEntity<>(terrariumSettings, headers);
            ResponseEntity<String> respone = restTemplate.exchange(
                    "http://" + terrarium.get().getIp() + "/terrarium/test2",
                    HttpMethod.POST,
                    body,
                    String.class);
        }

    }
}
