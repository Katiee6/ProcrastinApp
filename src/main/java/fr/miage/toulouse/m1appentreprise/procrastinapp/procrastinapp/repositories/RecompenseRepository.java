package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Recompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecompenseRepository extends CrudRepository<Recompense, Long> {

    List<Recompense> findRecompenseByTitreAndType(String titre, TypeRecompense type);

}
