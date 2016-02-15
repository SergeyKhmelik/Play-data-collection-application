package services;

import com.mysema.query.jpa.impl.JPAQuery;
import models.Field;
import models.Option;
import models.QOption;
import play.db.jpa.JPA;

import java.util.HashSet;
import java.util.Set;

/**
 * Declares mergeOptions method that perform merging Option objects with DB rows
 * by object name and field id
 *
 * Contains one default method mergeOptions(Set<Option> options, Field field)
 */
public interface OptionMerging {

    /**
     * Performs merging operation for Options set.
     *
     * Merges options by their name and id_field key.
     * If there is no option in DB - adds option with 0 identifier to resulting set.
     *
     * @param options Set of Option objects
     * @param field Field object
     *
     * @return Set of merged Option objects
     */
    default Set<Option> mergeOptions(Set<Option> options, Field field) {
        Set<Option> newOptions = new HashSet<>();
        if(options != null) {
            options.forEach(option -> {

                QOption qOption = QOption.option;
                JPAQuery query = new JPAQuery(JPA.em());
                Option dbOption = query
                        .from(qOption)
                        .where(qOption.value.eq(option.value), qOption.field.eq(field))
                        .singleResult(qOption);

                newOptions.add(dbOption != null ? dbOption : option);
            });
        }
        return newOptions;
    }
}
