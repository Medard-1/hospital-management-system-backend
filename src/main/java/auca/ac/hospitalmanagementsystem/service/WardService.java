package auca.ac.hospitalmanagementsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.Ward;
import auca.ac.hospitalmanagementsystem.repository.WardRepository;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;

    public String saveWard(Ward ward) {
        wardRepository.save(ward);
        return "Ward saved successfully";
    }

    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }

    public Optional<Ward> getWardById(UUID id) {
        return wardRepository.findById(id);
    }

    public String updateWard(UUID id, Ward updatedWard) {
        Optional<Ward> existing = wardRepository.findById(id);
        if (existing.isEmpty()) {
            return "The ward with that id does not exist";
        }

        Ward ward = existing.get();
        ward.setWardNumber(updatedWard.getWardNumber());
        ward.setName(updatedWard.getName());
        wardRepository.save(ward);
        return "Ward updated successfully";
    }

    public String deleteWard(UUID id) {
        if (!wardRepository.existsById(id)) {
            return "The ward with that id does not exist";
        }
        wardRepository.deleteById(id);
        return "Ward deleted successfully";
    }

    public List<Ward> getFreeWards() {
        return wardRepository.findFreeWards();
    }
}
