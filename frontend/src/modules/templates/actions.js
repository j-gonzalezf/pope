import backend from '../../backend';
import * as actionTypes from './actionTypes';

const createCycleCompleted = () => ({
    type: actionTypes.CREATE_CYCLE_COMPLETED
});

export const createCycle = (cycle, onSuccess, onErrors) => dispatch =>
    backend.templateService.createCycle(cycle,
        () => {
            dispatch(createCycleCompleted());
            onSuccess();
        },
        onErrors
    );

const getCyclesCompleted = getCycles => ({
    type: actionTypes.GET_CYCLES_COMPLETED,
    getCycles
});

export const getCycles = (trainerId, clientId, onSuccess, onErrors) => dispatch =>
    backend.templateService.getCycles(trainerId, clientId,
        getCycles => {
            dispatch(getCyclesCompleted(getCycles));
            onSuccess();
        },
        onErrors
    );

const getCycleCompleted = getCycle => ({
    type: actionTypes.GET_CYCLE_COMPLETED,
    getCycle
});

export const getCycle = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.getCycle(id,
        getCycle => {
            dispatch(getCycleCompleted(getCycle));
            onSuccess();
        },
        onErrors
    );

export const clearCycle = () => ({
    type: actionTypes.CLEAR_CYCLE
});

const updateCycleCompleted = getCycle => ({
    type: actionTypes.UPDATE_CYCLE_COMPLETED,
    getCycle
});

export const updateCycle = (cycle, onSuccess, onErrors) => dispatch =>
    backend.templateService.updateCycle(cycle,
        cycle => {
            dispatch(updateCycleCompleted(cycle));
            onSuccess();
        },
        onErrors
    );

const deleteCycleCompleted = () => ({
    type: actionTypes.DELETE_CYCLE_COMPLETED
});

export const deleteCycle = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.deleteCycle(id,
        () => {
            dispatch(deleteCycleCompleted());
            onSuccess();
        },
        onErrors
    );

const addExerciseCompleted = () => ({
    type: actionTypes.ADD_EXERCISE_COMPLETED
});

export const addExercise = (exercise, onSuccess, onErrors) => dispatch =>
    backend.templateService.addExercise(exercise,
        () => {
            dispatch(addExerciseCompleted());
            onSuccess();
        },
        onErrors
    );

const getExercisesCompleted = getExercises => ({
    type: actionTypes.GET_EXERCISES_COMPLETED,
    getExercises
});

export const getExercises = (trainerId, onSuccess, onErrors) => dispatch =>
    backend.templateService.getExercises(trainerId,
        getExercises => {
            dispatch(getExercisesCompleted(getExercises));
            onSuccess();
        },
        onErrors
    );

const getExerciseCompleted = getExercise => ({
    type: actionTypes.GET_EXERCISE_COMPLETED,
    getExercise
});

export const getExercise = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.getExercise(id,
        getExercise => {
            dispatch(getExerciseCompleted(getExercise));
            onSuccess();
        },
        onErrors
    );

export const clearExercise = () => ({
    type: actionTypes.CLEAR_EXERCISE
});

const updateExerciseCompleted = getExercise => ({
    type: actionTypes.UPDATE_EXERCISE_COMPLETED,
    getExercise
});

export const updateExercise = (exercise, onSuccess, onErrors) => dispatch =>
    backend.templateService.updateExercise(exercise,
        exercise => {
            dispatch(updateExerciseCompleted(exercise));
            onSuccess();
        },
        onErrors
    );

const deleteExerciseCompleted = () => ({
    type: actionTypes.DELETE_EXERCISE_COMPLETED
});

export const deleteExercise = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.deleteExercise(id,
        () => {
            dispatch(deleteExerciseCompleted());
            onSuccess();
        },
        onErrors
    );

const createTemplateCompleted = () => ({
    type: actionTypes.CREATE_TEMPLATE_COMPLETED
});

export const createTemplate = (template, onSuccess, onErrors) => dispatch =>
    backend.templateService.createTemplate(template,
        () => {
            dispatch(createTemplateCompleted());
            onSuccess();
        },
        onErrors
    );

const getTemplatesCompleted = getTemplates => ({
    type: actionTypes.GET_TEMPLATES_COMPLETED,
    getTemplates
});

export const getTemplates = (cycleId, onSuccess, onErrors) => dispatch =>
    backend.templateService.getTemplates(cycleId,
        getTemplates => {
            dispatch(getTemplatesCompleted(getTemplates));
            onSuccess();
        },
        onErrors
    );

const getTemplateCompleted = getTemplate => ({
    type: actionTypes.GET_TEMPLATE_COMPLETED,
    getTemplate
});

export const getTemplate = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.getTemplate(id,
        getTemplate => {
            dispatch(getTemplateCompleted(getTemplate));
            onSuccess();
        },
        onErrors
    );

const updateTemplateCompleted = getTemplate => ({
    type: actionTypes.UPDATE_TEMPLATE_COMPLETED,
    getTemplate
});

export const updateTemplate = (template, onSuccess, onErrors) => dispatch =>
    backend.templateService.updateTemplate(template,
        () => {
            dispatch(updateTemplateCompleted(template));
            onSuccess();
        },
        onErrors
    );

const deleteTemplateCompleted = () => ({
    type: actionTypes.DELETE_TEMPLATE_COMPLETED
});

export const deleteTemplate = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.deleteTemplate(id,
        () => {
            dispatch(deleteTemplateCompleted());
            onSuccess();
        },
        onErrors
    );

const addTemplateRowCompleted = () => ({
    type: actionTypes.ADD_TEMPLATE_ROW_COMPLETED
});

export const addTemplateRow = (row, onSuccess, onErrors) => dispatch =>
    backend.templateService.addTemplateRow(row,
        () => {
            dispatch(addTemplateRowCompleted());
            onSuccess();
        },
        onErrors
    );

const getTemplateRowsCompleted = getTemplateRows => ({
    type: actionTypes.GET_TEMPLATE_ROWS_COMPLETED,
    getTemplateRows
});

export const getTemplateRows = (templateId, onSuccess, onErrors) => dispatch =>
    backend.templateService.getTemplateRows(templateId,
        getTemplateRows => {
            dispatch(getTemplateRowsCompleted(getTemplateRows));
            onSuccess();
        },
        onErrors
    );

const updateTemplateRowCompleted = () => ({
    type: actionTypes.UPDATE_TEMPLATE_ROW_COMPLETED
});

export const updateTemplateRow = (row, onSuccess, onErrors) => dispatch =>
    backend.templateService.updateTemplateRow(row,
        () => {
            dispatch(updateTemplateRowCompleted());
            onSuccess();
        },
        onErrors
    );

const deleteTemplateRowCompleted = () => ({
    type: actionTypes.DELETE_TEMPLATE_ROW_COMPLETED
});

export const deleteTemplateRow = (templateId, id, onSuccess, onErrors) => dispatch =>
    backend.templateService.deleteTemplateRow(templateId, id,
        () => {
            dispatch(deleteTemplateRowCompleted());
            onSuccess();
        },
        onErrors
    );
