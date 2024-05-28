const getModuleState = state => state.templates;

export const getCycles = state => {
    return getModuleState(state).getCycles;
}
