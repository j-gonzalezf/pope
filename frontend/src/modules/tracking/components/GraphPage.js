import React, { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { Chart } from 'react-charts';

import * as selectors from '../selectors';
import './GraphPage.css';

const GraphPage = () => {

    const sensations = useSelector(selectors.getSensations);

    const { sensationKey } = useParams();

    const [timeRange, setTimeRange] = useState(7);
    const [weekBtn, setWeekBtn] = useState(true);
    const [monthBtn, setMonthBtn] = useState(false);
    const [yearBtn, setYearBtn] = useState(false);

    const filteredData = sensations.map(sensation => {
        const date = new Date(sensation.sensationDate);
        const formattedDate = date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
        return {
            primary: formattedDate,
            secondary: sensation[sensationKey],
        };
    });

    const lastData = filteredData.slice(-timeRange);

    const chartData = React.useMemo(
        () => [
            {
                label: `${sensationKey} Data`,
                data: lastData,
            },
        ],
        [lastData, sensationKey]
    );

    const axes = React.useMemo(
        () => [
            {
                primary: true, type: 'ordinal', position: 'bottom',
                format: d => {
                    if (timeRange !== 7) {
                        const step = Math.ceil(lastData.length / 7);
                        const index = lastData.findIndex(data => data.primary === d);
                        return index % step === 0 ? d : '';
                    }
                    return d;
                }
            },
            { type: 'linear', position: 'left', hardMin: 1, hardMax: 5, format: d => (d % 1 === 0 ? Math.floor(d) : '') },
        ],
        [timeRange, lastData]
    );

    const getSeriesStyle = React.useCallback(
        () => ({
            stroke: '#e6af2e',
        }),
        []
    );

    const getDatumStyle = React.useCallback(
        () => ({
            r: 0,
            stroke: '#e6af2e',
            fill: '#e6af2e',
        }),
        []
    );

    return (
        <div fluid="true" className='GraphPage'>
            <h3 className="title">
                <FormattedMessage id={`project.tracking.${sensationKey}`} />
            </h3>
            <div className='GraphContainer'>
                <div className="btn-group" role="group" aria-label="Time range">
                    <button className={weekBtn ? "btn primary" : "btn notSelected"} onClick={() => { setTimeRange(7); setWeekBtn(true); setMonthBtn(false); setYearBtn(false) }}>
                        <FormattedMessage id="project.tracking.week" />
                    </button>
                    <button className={monthBtn ? "btn primary" : "btn notSelected"} onClick={() => { setTimeRange(30); setWeekBtn(false); setMonthBtn(true); setYearBtn(false) }}>
                        <FormattedMessage id="project.tracking.month" />
                    </button>
                    <button className={yearBtn ? "btn primary" : "btn notSelected"} onClick={() => { setTimeRange(365); setWeekBtn(false); setMonthBtn(false); setYearBtn(true) }}>
                        <FormattedMessage id="project.tracking.year" />
                    </button>
                </div>
                <br />
                <br />
                <Chart
                    data={chartData}
                    axes={axes}
                    getSeriesStyle={getSeriesStyle}
                    getDatumStyle={getDatumStyle}
                    className="Graph"
                />
            </div>
        </div>
    );
};

export default GraphPage;
