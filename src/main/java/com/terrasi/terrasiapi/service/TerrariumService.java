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

import javax.swing.text.html.Option;
import java.lang.reflect.GenericSignatureFormatError;
import java.util.Collections;
import java.util.Map;
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
        if(checkAuthForTerrarium(terrarium, user)){
            return terrarium.get();
        }
        return null;
    }

    public boolean saveTerrariumSettings(Long id, String accessToken, TerrariumSettings settings) throws UnauthorizedException, NotFoundException, ForbiddenException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(checkAuthForTerrarium(terrarium, user)){
            terrariumSettingsRepository.save(settings);
            return true;
        }
        return false;
    }

    public boolean updateTerrariumName(Long id, String name, String accessToken) throws UnauthorizedException, NotFoundException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        if(checkAuthForTerrarium(terrarium, user)){
            terrarium.get().setName(name);
            terrariumRepository.save(terrarium.get());
            return true;
        }
        return false;
    }

    public void sendTerrariumSettings(TerrariumSettings terrariumSettings, String accessToken){
        Optional<Terrarium> terrarium = terrariumRepository.getByTerrariumSettingsId(terrariumSettings.getId());
        if(terrarium.isPresent()){
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<TerrariumSettings> body = new HttpEntity<>(terrariumSettings, headers);
            ResponseEntity<String> response = rest.exchange(
                    "http://" + terrarium.get().getIp() + "/terrarium/settings",
                    HttpMethod.POST,
                    body,
                    String.class);
        }

    }

    public void bulbTurnOnOf(Long id, String accessToken) throws UnauthorizedException, NotFoundException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(checkAuthForTerrarium(terrarium, user)){
            terrarium.get().getTerrariumSettings().setIsBulbWorking(!terrarium.get().getTerrariumSettings().getIsBulbWorking());
            terrariumSettingsRepository.save(terrarium.get().getTerrariumSettings());

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Map<String, Boolean>> body = new HttpEntity<>(
                    Collections.singletonMap("bulbTurnOn", terrarium.get().getTerrariumSettings().getIsBulbWorking()),
                    headers);
            ResponseEntity<String> response = rest.exchange(
                    "http://" + "192.168.55.103:8081" + "/terrarium/bulbOnOf",
                    HttpMethod.POST,
                    body,
                    String.class
            );
            System.out.println(response.getBody());
        }
    }

    public boolean checkAuthForTerrarium(Optional<Terrarium> terrarium, Optional<User> user) throws UnauthorizedException, NotFoundException {
        if(user.isPresent()){
            if(terrarium.isPresent()){
                if(terrarium.get().getUser().getId().equals(user.get().getId())){
                    return true;
                }
                else {
                    throw new UnauthorizedException();
                }
            }
            else {
                throw new NotFoundException();
            }
        }
        throw new UnauthorizedException();

    }
}
