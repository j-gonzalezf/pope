import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { BsCheckSquareFill, BsFillPlusCircleFill, BsXSquareFill } from "react-icons/bs";
import './TemplatesList.css';

import { useEffect, useRef, useState } from 'react';
import Autosuggest from 'react-autosuggest';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as userSelectors from '../../users/selectors';
import * as selectors from '../selectors';

const AddTemplateRow = () => {

    const dispatch = useDispatch();

    const user = useSelector(userSelectors.getUser);
    const getExercises = useSelector(selectors.getExercises);
    const { templateId } = useParams();
    const wrapperRef = useRef(null);

    const [selectedExercise, setSelectedExercise] = useState(null);
    const [inputValue, setInputValue] = useState('');
    const [series, setSeries] = useState('');
    const [reps, setReps] = useState('');
    const [weight, setWeight] = useState('');
    const [error, setError] = useState(null);
    const [showAddInput, setShowAddInput] = useState(true);
    const [suggestions, setSuggestions] = useState([]);
    const [isSuggestionSelected, setIsSuggestionSelected] = useState(false);
    const [isValid, setIsValid] = useState(true);

    let form;

    // Para escapar caracteres especiales y poder usar texto literal
    function escapeRegexCharacters(str) {
        return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    }

    function getSuggestions(value) {

        const escapedValue = escapeRegexCharacters(value.trim());

        if (escapedValue === '') {
            return [];
        }

        const regex = new RegExp('^' + escapedValue, 'i');

        return getExercises.filter(exercise => regex.test(exercise.name));

    }

    // Función para retornar el nombre de la sugerencia
    function getSuggestionValue(suggestion) {
        return suggestion.name;
    }

    // Función para renderizar la lista de sugerencias
    function renderSuggestion(suggestion) {
        return (
            <div key={suggestion.id} className="suggestion">
                {suggestion.name}
            </div>
        );
    }

    // Actualiza el estado value cuando el valor del input cambia
    const onExerciseChange = (event, { newValue }) => {
        setInputValue(newValue);
        setIsSuggestionSelected(false);
    };

    // Actualiza el estado suggestions con las sugerencias obtenidas
    const onSuggestionsFetchRequested = ({ value }) => {
        const filteredExercises = getSuggestions(value);
        setSuggestions(filteredExercises);
    };

    const onSuggestionsClearRequested = () => {
        setSuggestions([]);
    };

    const onSuggestionSelected = (event, { suggestion }) => {
        setSelectedExercise(suggestion);
        setIsSuggestionSelected(true);
    };

    const handleSubmit = event => {

        event.preventDefault();

        if (selectedExercise !== null) {
            setIsValid(true);

            const seriesValue = series === '0' ? null : series;
            const repsValue = reps === '0' ? null : reps;
            const weightValue = weight === '0' ? null : weight;

            dispatch(actions.addTemplateRow(
                {
                    exerciseName: selectedExercise.name,
                    series: seriesValue || null,
                    repetitions: repsValue || null,
                    weight: weightValue || null,
                    exerciseId: selectedExercise.id,
                    templateId: templateId
                },
                () => {
                    onClose();
                    dispatch(actions.getTemplateRows(templateId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)));
        } else {
            setIsValid(false);
            setInputValue('');
            form.reportValidity();
        }

    }

    const onClose = () => {
        setShowAddInput(false);
        setSelectedExercise(null);
        setInputValue('');
        setSeries('');
        setReps('');
        setWeight('');
        setSuggestions([]);
        setIsSuggestionSelected(false);
    }

    useEffect(() => {
        dispatch(actions.getExercises(user.id,
            () => {
                setSuggestions(getExercises);
            },
            errors => setError(errors)));
        // eslint-disable-next-line
    }, [dispatch, user.id]);

    /* Función que detecta clicks fuera del componente para cerrarlo */
    useEffect(() => {
        function handleClickOutside(event) {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setShowAddInput(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    return (

        showAddInput ? (
            <div ref={wrapperRef} className="addTemplateRow" >
                <Form
                    ref={node => form = node}
                    className="needs-validation"
                    noValidate
                    onSubmit={e => handleSubmit(e)}
                >
                    <Form.Group className="mb-3 templateRow">

                        <Autosuggest
                            suggestions={suggestions}
                            onSuggestionsFetchRequested={onSuggestionsFetchRequested}
                            onSuggestionsClearRequested={onSuggestionsClearRequested}
                            getSuggestionValue={getSuggestionValue}
                            renderSuggestion={renderSuggestion}
                            inputProps={{
                                className: `form-control ${isValid ? '' : 'is-invalid'}`,
                                id: "exercise",
                                name: "exercise",
                                placeholder: "Ejercicio",
                                value: inputValue,
                                onChange: onExerciseChange,
                                required: true,
                                autoFocus: true
                            }}
                            onSuggestionSelected={onSuggestionSelected}
                            renderSuggestionsContainer={({ containerProps, children, query }) => {
                                return (
                                    <div {...containerProps} className="suggestions-container">
                                        {children}
                                        {
                                            // Si no hay sugerencias o el usuario ha escrito algo mal, muestra el enlace
                                            !children && query && !isSuggestionSelected &&
                                            <div className="no-suggestions">
                                                <FormattedMessage id="project.templates.exerciseNotFound" />
                                                <br />
                                                <Link className="link" to="/templates/exercises">
                                                    <FormattedMessage id="project.templates.exerciseNotFound.link" />
                                                </Link>
                                            </div>
                                        }
                                    </div>
                                );
                            }}
                        />
                        <Form.Control.Feedback type="invalid">
                            Por favor, selecciona un ejercicio existente.
                        </Form.Control.Feedback>

                        <Form.Control
                            type="number"
                            className="form-control"
                            id="series"
                            name="series"
                            placeholder="Series"
                            value={series}
                            onChange={event => setSeries(event.target.value)}
                            min={0}
                        />

                        <Form.Control
                            type="number"
                            className="form-control"
                            id="reps"
                            name="reps"
                            placeholder="Reps"
                            value={reps}
                            onChange={event => setReps(event.target.value)}
                            min={0}
                        />

                        <Form.Control
                            type="number"
                            step="0.01"
                            className="form-control"
                            id="weight"
                            name="weight"
                            placeholder="Peso"
                            value={weight}
                            onChange={event => setWeight(event.target.value)}
                            min={0}
                        />

                        <div className="form-buttons">
                            <Button className="primary template" type="submit">
                                <BsCheckSquareFill className="checkIconStyle" color='#e6af2e' size={20} />
                            </Button>
                            <Button className="primary template" onClick={onClose}>
                                <BsXSquareFill className="crossIconStyle" color='gray' size={20} />
                            </Button>
                        </div>
                    </Form.Group>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Form>
            </div>
        ) : (
            <Button className="primary cycle" onClick={() => setShowAddInput(true)} >
                <BsFillPlusCircleFill className="plusIconStyle cycle" />
                <span>
                    <b><FormattedMessage id="project.templates.addTemplateRow" /></b>
                </span>
            </Button>
        )
    );

}

export default AddTemplateRow;
