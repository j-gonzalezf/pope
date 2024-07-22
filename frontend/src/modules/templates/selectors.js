const getModuleState = state => state.templates;

export const getCycles = state =>
    getModuleState(state).getCycles;

export const getCycle = state =>
    getModuleState(state).getCycle;

export const getExercises = state =>
    getModuleState(state).getExercises;

export const getExercise = state =>
    getModuleState(state).getExercise;

export const getTemplates = state =>
    getModuleState(state).getTemplates;

export const getTemplate = state =>
    getModuleState(state).getTemplate;

export const getTemplateRows = state =>
    getModuleState(state).getTemplateRows;
