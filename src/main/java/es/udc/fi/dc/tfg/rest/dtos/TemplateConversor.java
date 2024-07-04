package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import java.time.LocalDateTime;

/**
 * Clase TemplateConversor.
 */
public class TemplateConversor {

    /**
     * Instancia un nuevo template conversor
     */
    private TemplateConversor() {
    }

    /**
     * Convierte un objeto Templates a un objeto TemplateDto
     *
     * @param template el objeto Templates a convertir
     * @return un nuevo objeto TemplateDto que contiene los mismos datos que el
     * objeto Templates proporcionado
     */
    public static final TemplateDto toTemplateDto(Templates template) {
        return new TemplateDto(template.getId(), template.getName(),
                template.getCreationDate().toString(), template.getCycle().getId());
    }

    /**
     * Convierte un objeto TemplateDto a un objeto Templates
     *
     * @param templateDto el objeto TemplateDto a convertir
     * @param cycle el ciclo del TemplateDto
     * @return un nuevo objeto Templates que contiene los mismos datos que el
     * objeto TemplateDto proporcionado
     */
    public static final Templates toTemplates(TemplateDto templateDto, TrainingCycles cycle) {
        return new Templates(templateDto.getName(),
                LocalDateTime.parse(templateDto.getCreationDate()), cycle);

    }

    /**
     * Convierte un objeto TemplateRows a un objeto TemplateRowDto
     *
     * @param templateRow el objeto TemplateRows a convertir
     * @return un nuevo objeto TemplateRowDto que contiene los mismos datos que
     * el objeto TemplateRows proporcionado
     */
    public static final TemplateRowDto toTemplateRowDto(TemplateRows templateRow) {
        return new TemplateRowDto(templateRow.getId(), templateRow.getSeries(),
                templateRow.getRepetitions(), templateRow.getWeight(),
                templateRow.getExercise().getId(), templateRow.getTemplate().getId());
    }

    /**
     * Convierte un objeto TemplateRowDto a un objeto TemplateRows
     *
     * @param templateRowDto el objeto TemplateRowDto a convertir
     * @param exercise el ejercicio del TemplateRowDto
     * @param template la plantilla del TemplateRowDto
     * @return un nuevo objeto TemplateRows que contiene los mismos datos que el
     * objeto TemplateRowDto proporcionado
     */
    public static final TemplateRows toTemplateRows(TemplateRowDto templateRowDto, Exercises exercise, Templates template) {
        return new TemplateRows(templateRowDto.getSeries(), templateRowDto.getRepetitions(),
                templateRowDto.getWeight(), exercise, template);

    }

}
