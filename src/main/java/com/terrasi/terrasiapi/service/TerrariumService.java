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
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class TerrariumService {

    private final UserRepository userRepository;
    private final TerrariumRepository terrariumRepository;
    private final TerrariumSettingsRepository terrariumSettingsRepository;
    private final RabbitTemplate rabbitTemplate;

    public TerrariumService(UserRepository userRepository, TerrariumRepository terrariumRepository,
                            TerrariumSettingsRepository terrariumSettingsRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.terrariumRepository = terrariumRepository;
        this.terrariumSettingsRepository = terrariumSettingsRepository;
        this.rabbitTemplate = rabbitTemplate;
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
        terrarium.ifPresent(value -> rabbitTemplate.convertAndSend("test", value.getTerrariumSettings()));
    }

    public void bulbTurnOnOf(Long id, String accessToken) throws UnauthorizedException, NotFoundException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(checkAuthForTerrarium(terrarium, user)){
            terrarium.get().getTerrariumSettings().setIsBulbWorking(!terrarium.get().getTerrariumSettings().getIsBulbWorking());
            terrariumSettingsRepository.save(terrarium.get().getTerrariumSettings());
            rabbitTemplate.convertAndSend("test", terrarium.get().getTerrariumSettings());
        }
    }

    public void humidifierTurnOnOf(Long id, String accessToken) throws UnauthorizedException, NotFoundException {
        JwtModel jwtModel = JwtUtils.parseAccessToken(accessToken);
        Optional<User> user = userRepository.findByUsername(jwtModel.getUsername());
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(checkAuthForTerrarium(terrarium, user)){
            terrarium.get().getTerrariumSettings().setIsHumidifierWorking(!terrarium.get().getTerrariumSettings().getIsHumidifierWorking());
            terrariumSettingsRepository.save(terrarium.get().getTerrariumSettings());
            rabbitTemplate.convertAndSend("test", terrarium.get().getTerrariumSettings());
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
