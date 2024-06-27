const getModuleState = state => state.templates;

export const getCycles = state => {
    return getModuleState(state).getCycles;
}

export const getCycle = state => {
    return getModuleState(state).getCycle;
}

export const getExercises = state => {
    return getModuleState(state).getExercises;
}
