const roomCode = sessionStorage.getItem('roomCode');
const currentUsername = "GM";
let gameState = null;

document.getElementById('headerRoomCode').innerText = roomCode;

let stopwatchSeconds = 0;
let stopwatchIntervalId = null;

function formatStopwatchTime(totalSec) {
    const mins = Math.floor(totalSec / 60);
    const secs = totalSec % 60;
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

function renderStopwatch() {
    const displayEl = document.getElementById('stopwatchDisplay');
    const secLabelEl = document.getElementById('stopwatchSecondsLabel');
    const toggleIcon = document.getElementById('stopwatchToggleIcon');
    const toggleText = document.getElementById('stopwatchToggleText');
    const toggleBtn = document.getElementById('stopwatchToggleBtn');

    if (displayEl) displayEl.textContent = formatStopwatchTime(stopwatchSeconds);
    if (secLabelEl) secLabelEl.textContent = `${stopwatchSeconds}s`;

    const isRunning = stopwatchIntervalId !== null;
    if (toggleIcon) {
        toggleIcon.className = isRunning ? 'fa-solid fa-pause' : 'fa-solid fa-play';
    }
    if (toggleText) {
        toggleText.textContent = isRunning ? 'PAUSE' : 'START';
    }
    if (toggleBtn) {
        toggleBtn.className = isRunning
            ? 'px-2.5 py-1.5 bg-amber-300 hover:bg-amber-200 text-slate-950 font-bold rounded text-xs transition-colors flex items-center space-x-1'
            : 'px-2.5 py-1.5 bg-amber-500 hover:bg-amber-400 text-slate-950 font-bold rounded text-xs transition-colors flex items-center space-x-1';
    }
}

function toggleStopwatch() {
    if (stopwatchIntervalId !== null) {
        clearInterval(stopwatchIntervalId);
        stopwatchIntervalId = null;
    } else {
        stopwatchIntervalId = setInterval(() => {
            stopwatchSeconds += 1;
            renderStopwatch();
        }, 1000);
    }
    renderStopwatch();
}

function resetStopwatch() {
    if (stopwatchIntervalId !== null) {
        clearInterval(stopwatchIntervalId);
        stopwatchIntervalId = null;
    }
    stopwatchSeconds = 0;
    renderStopwatch();
}

renderStopwatch();

const notesStorageKey = `gmNotes_${roomCode || 'default'}`;
const gmNotesInput = document.getElementById('gmNotesInput');
if (gmNotesInput) {
    gmNotesInput.value = sessionStorage.getItem(notesStorageKey) || '';
    gmNotesInput.addEventListener('input', () => {
        sessionStorage.setItem(notesStorageKey, gmNotesInput.value);
    });
}

function clearGmNotes() {
    if (!gmNotesInput) return;
    gmNotesInput.value = '';
    sessionStorage.removeItem(notesStorageKey);
}

function renderStageBadge(state) {
    const stage = state?.stage || 'MAIN_PHASE';
    const isVoting = stage === 'VOTING';
    const cls = isVoting
        ? 'text-[10px] px-2 py-0.5 rounded border font-bold tracking-wider bg-rose-500/20 text-rose-300 border-rose-500/60 animate-pulse shrink-0'
        : 'text-[10px] px-2 py-0.5 rounded border font-bold tracking-wider bg-emerald-500/10 text-emerald-400 border-emerald-500/30 shrink-0';

    const headerBadge = document.getElementById('headerStageBadge');
    const panelBadge = document.getElementById('panelStageBadge');

    if (headerBadge) {
        headerBadge.textContent = stage;
        headerBadge.className = cls;
    }
    if (panelBadge) {
        panelBadge.textContent = stage;
        panelBadge.className = cls;
    }
}

function syncForceVoteSelects(players) {
    const voterSelect = document.getElementById('forceVoterSelect');
    const targetSelect = document.getElementById('forceTargetSelect');
    const forceBtn = document.getElementById('forceVoteBtn');
    if (!voterSelect || !targetSelect) return;

    const prevVoter = voterSelect.value;
    const prevTarget = targetSelect.value;

    const alivePlayers = players
        .map((player, index) => ({ player, slotNumber: index + 1 }))
        .filter(({ player }) => player && player.isAlive);

    const optionsHtml = alivePlayers
        .map(({ player, slotNumber }) => `<option value="${player.nickname.replace(/"/g, '&quot;')}">#${slotNumber} ${player.nickname}</option>`)
        .join('');

    voterSelect.innerHTML = `<option value="">Select voter...</option>${optionsHtml}`;
    targetSelect.innerHTML = `<option value="">Select target...</option>${optionsHtml}`;

    if (alivePlayers.some(x => x.player.nickname === prevVoter)) {
        voterSelect.value = prevVoter;
    }
    if (alivePlayers.some(x => x.player.nickname === prevTarget)) {
        targetSelect.value = prevTarget;
    }

    if (forceBtn) {
        forceBtn.disabled = alivePlayers.length === 0;
    }
}

function sendVote(voterNickname, targetNickname) {
    if (!voterNickname || !targetNickname) {
        alert('Choose both voter and target');
        return;
    }
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.warn('WebSocket does not run');
        return;
    }

    const voteRequest = {
        type: "VOTE",
        nickname: voterNickname,
        targetNickname: targetNickname
    };
    ws.send(JSON.stringify(voteRequest));
}

function submitForceVote() {
    const voterNickname = document.getElementById('forceVoterSelect')?.value;
    const targetNickname = document.getElementById('forceTargetSelect')?.value;
    sendVote(voterNickname, targetNickname);
}

function renderVotesBadgeHtml(player, state) {
    const voters = Array.isArray(player?.votesFromPlayers) ? player.votesFromPlayers : [];
    const voteCount = voters.length;
    const isVoting = state?.stage === 'VOTING';

    if (!isVoting && voteCount === 0) {
        return '';
    }

    const circlesHtml = voters.map(voterSlot => {
        const voterPlayer = state?.players?.[voterSlot - 1];
        const voterName = voterPlayer ? voterPlayer.nickname : `Slot #${voterSlot}`;
        return `
                    <span title="Vote from ${voterName} (Slot #${voterSlot})"
                          class="inline-flex items-center justify-center w-5 h-5 rounded-full text-[10px] font-bold bg-rose-500/25 text-rose-200 border border-rose-400/80 shadow-[0_0_8px_rgba(244,63,94,0.35)]">
                        ${voterSlot}
                    </span>
                `;
    }).join('');

    return `
                <div class="mt-1.5 pt-1.5 border-t border-slate-800/90 flex items-center justify-between gap-1.5">
                    <div class="flex items-center flex-wrap gap-1 min-h-[20px]">
                        ${circlesHtml || `<span class="text-[10px] text-slate-500 italic">No votes</span>`}
                    </div>
                    <span class="text-[10px] font-bold px-1.5 py-0.5 rounded bg-rose-950/80 text-rose-300 border border-rose-500/40 shrink-0">
                        ${voteCount} votes
                    </span>
                </div>
            `;
}

function syncPlayerSelect(selectId, players, previousValue) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = '<option value="">Select Survivor...</option>';
    players.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.nickname;
        opt.innerText = p.nickname;
        select.appendChild(opt);
    });

    if (previousValue && players.some(p => p.nickname === previousValue)) {
        select.value = previousValue;
    }
}

function renderGmState(state) {
    if (!state) return;

    renderStageBadge(state);

    if (state.catastropheDescription) {
        const el = document.getElementById('catastropheDesc');
        if (el) el.innerText = state.catastropheDescription;
    }
    if (state.bunkerDescription) {
        const el = document.getElementById('bunkerDesc');
        if (el) el.innerText = state.bunkerDescription;
    }
    if (state.bunkerInventory) {
        const el = document.getElementById('bunkerInv');
        if (el) el.innerText = "Inventory: " + state.bunkerInventory;
    }

    const selectPlayer = document.getElementById('selectPlayer');
    const selectRandomTraitPlayer = document.getElementById('selectRandomTraitPlayer');
    const selectCustomTraitPlayer = document.getElementById('selectCustomTraitPlayer');
    const selectStatusPlayer = document.getElementById('selectStatusPlayer');

    const prevPlayer = selectPlayer ? selectPlayer.value : '';
    const prevRandomPlayer = selectRandomTraitPlayer ? selectRandomTraitPlayer.value : '';
    const prevCustomPlayer = selectCustomTraitPlayer ? selectCustomTraitPlayer.value : '';
    const prevStatusPlayer = selectStatusPlayer ? selectStatusPlayer.value : '';

    const players = state.players || [];

    syncPlayerSelect('selectPlayer', players, prevPlayer);
    syncPlayerSelect('selectRandomTraitPlayer', players, prevRandomPlayer);
    syncPlayerSelect('selectCustomTraitPlayer', players, prevCustomPlayer);

    if (selectStatusPlayer) {
        selectStatusPlayer.innerHTML = '<option value="">Select Survivor...</option>';
        players.forEach(p => {
            const opt2 = document.createElement('option');
            opt2.value = p.nickname;
            opt2.innerText = `${p.nickname} (${p.isAlive ? 'Alive' : 'Dead'})`;
            selectStatusPlayer.appendChild(opt2);
        });
        if (prevStatusPlayer && players.some(p => p.nickname === prevStatusPlayer)) {
            selectStatusPlayer.value = prevStatusPlayer;
        }
    }

    syncForceVoteSelects(players);

    const grid = document.getElementById('playersGrid');
    if (!grid) return;
    grid.innerHTML = '';

    for (let i = 0; i < 12; i++) {
        const slotNumber = i + 1;
        const player = players[i] || null;
        const isOccupied = !!player;
        const nickname = player ? player.nickname : `SLOT #${slotNumber}`;
        const isAlive = player ? player.isAlive : true;

        const renderGmTraitItem = (traitObj, fallbackName) => {
            if (!isOccupied || !traitObj) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">${fallbackName}</div>`;
            }

            const isOpenToAll = traitObj.isOpen;
            const statusBadge = isOpenToAll
                ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-1" title="Открыта всем"><i class="fa-solid fa-eye"></i></span>`
                : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-1" title="Закрыта (видна только ГМу и игроку)"><i class="fa-solid fa-lock"></i></span>`;

            return `
                <div class="flex items-start justify-between text-white text-xs py-1" title="${traitObj.value}">
                    <span class="break-words leading-tight ${isOpenToAll ? 'text-emerald-200' : 'text-slate-300'}">${traitObj.value}</span>
                    ${statusBadge}
                </div>
            `;
        };

        const renderGmHealthWithScore = (healthObj, scoreObj) => {
            if (!isOccupied) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">Health</div>`;
            }

            const hasHealth = Boolean(healthObj) && healthObj.value !== undefined && healthObj.value !== null && String(healthObj.value) !== '';
            const hasScore = Boolean(scoreObj) && scoreObj.value !== undefined && scoreObj.value !== null && String(scoreObj.value) !== '';

            if (!hasHealth && !hasScore) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">Health</div>`;
            }

            const healthHtml = hasHealth
                ? `<span class="break-words leading-tight ${healthObj.isOpen ? 'text-emerald-200' : 'text-slate-300'}">${healthObj.value}</span>`
                : `<span class="text-slate-600 italic leading-tight">Health</span>`;

            const healthEyeLock = hasHealth
                ? (healthObj.isOpen
                    ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-0.5" title="Здоровье открыто всем"><i class="fa-solid fa-eye"></i></span>`
                    : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-0.5" title="Здоровье закрыто"><i class="fa-solid fa-lock"></i></span>`)
                : '';

            const scoreText = hasScore ? `${scoreObj.value}%` : '';
            const scoreHtml = hasScore
                ? `<span class="text-[11px] font-bold text-rose-400 bg-rose-950/60 border border-rose-500/40 px-1.5 py-0.2 rounded shrink-0">${scoreText}</span>`
                : '';

            const scoreEyeLock = hasScore
                ? (scoreObj.isOpen
                    ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-0.5" title="Health score открыт всем"><i class="fa-solid fa-eye"></i></span>`
                    : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-0.5" title="Health score закрыт"><i class="fa-solid fa-lock"></i></span>`)
                : '';

            const fullTitle = [
                hasHealth ? healthObj.value : null,
                hasScore ? `Health score: ${scoreText}` : null
            ].filter(Boolean).join(' — ');

            return `
                <div class="flex items-start justify-between gap-1 text-xs py-1" title="${fullTitle}">
                    <div class="flex items-baseline flex-wrap gap-1 min-w-0">
                        ${healthHtml}
                        ${scoreHtml}
                    </div>
                    <div class="flex items-center shrink-0">
                        ${healthEyeLock}
                        ${scoreEyeLock}
                    </div>
                </div>
            `;
        };

        const renderGmHeaderMeta = (sexObj, ageObj, bioObj) => {
            if (!isOccupied) return '';

            const sexText = sexObj?.value ? sexObj.value : 'Gender';
            const sexIcon = sexObj ? (sexObj.isOpen
                ? `<i class="fa-solid fa-eye text-[9px] text-emerald-400 ml-1" title="Открыто всем"></i>`
                : `<i class="fa-solid fa-lock text-[9px] text-amber-500 ml-1" title="Закрыто"></i>`) : '';

            const ageText = ageObj?.value !== undefined && ageObj?.value !== null && String(ageObj?.value) !== '' ? `${ageObj.value} yrs` : 'Age';
            const ageIcon = ageObj ? (ageObj.isOpen
                ? `<i class="fa-solid fa-eye text-[9px] text-emerald-400 ml-1" title="Открыто всем"></i>`
                : `<i class="fa-solid fa-lock text-[9px] text-amber-500 ml-1" title="Закрыто"></i>`) : '';

            const bioText = bioObj?.value ? bioObj.value : 'Biology';
            const bioIcon = bioObj ? (bioObj.isOpen
                ? `<i class="fa-solid fa-eye text-[9px] text-emerald-400 ml-1" title="Открыто всем"></i>`
                : `<i class="fa-solid fa-lock text-[9px] text-amber-500 ml-1" title="Закрыто"></i>`) : '';

            return `
                <div class="flex items-center flex-wrap gap-1.5 mt-1 text-[11px] text-slate-300 font-mono">
                    <span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800 inline-flex items-center" title="${sexText}">
                        <i class="fa-solid fa-venus-mars mr-1 text-amber-500"></i>${sexText}${sexIcon}
                    </span>
                    <span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800 inline-flex items-center" title="${ageText}">
                        <i class="fa-solid fa-cake-candles mr-1 text-amber-500"></i>${ageText}${ageIcon}
                    </span>
                    <span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800 inline-flex items-center" title="${bioText}">
                        <i class="fa-solid fa-dna mr-1 text-amber-500"></i>${bioText}${bioIcon}
                    </span>
                </div>
            `;
        };

        const card = document.createElement('div');
        card.className = `bg-slate-900/95 border rounded p-2.5 flex flex-col justify-between ${
            isOccupied
                ? (isAlive ? 'border-amber-500/70 shadow-[0_0_10px_rgba(245,158,11,0.1)]' : 'border-rose-900/60 opacity-65')
                : 'border-slate-800/80 opacity-40'
        }`;

        const sex = player ? player.sex : null;
        const biology = player ? player.biology : null;
        const age = player ? player.age : null;
        const profession = player ? player.profession : null;
        const health = player ? player.health : null;
        const healthScore = player ? player.healthScore : null;
        const hobby = player ? player.hobby : null;
        const phobia = player ? player.phobia : null;
        const baggage = player ? player.baggage : null;
        const fact1 = player ? player.factOne : null;
        const fact2 = player ? player.factTwo : null;
        const votesHtml = isOccupied ? renderVotesBadgeHtml(player, state) : '';
        const metaHtml = renderGmHeaderMeta(sex, age, biology);

        card.innerHTML = `
            <div class="border-b border-slate-800 pb-1.5 mb-1.5 shrink-0">
                <div class="flex items-center justify-between gap-1">
                    <span class="text-xs sm:text-sm font-bold ${isOccupied ? (isAlive ? 'text-amber-400' : 'text-rose-400 line-through') : 'text-slate-600'} truncate">
                        ${slotNumber}. ${nickname}
                    </span>
                    <span class="text-[9px] px-1.5 py-0.5 rounded uppercase font-semibold shrink-0 ${
            isOccupied
                ? (isAlive ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30')
                : 'text-slate-600'
        }">
                        ${isOccupied ? (isAlive ? 'Alive' : 'Dead / Exiled') : 'Empty'}
                    </span>
                </div>
                ${metaHtml}
                ${votesHtml}
            </div>

            <div class="grid grid-cols-2 gap-2 text-xs">
                <div class="flex flex-col bg-slate-950/80 p-2 rounded border border-slate-800 justify-between space-y-1">
                    ${renderGmTraitItem(profession, 'Profession')}
                    ${renderGmHealthWithScore(health, healthScore)}
                    ${renderGmTraitItem(hobby, 'Hobby')}
                    ${renderGmTraitItem(phobia, 'Phobia')}
                </div>

                <div class="flex flex-col bg-slate-950/80 p-2 rounded border border-slate-800 justify-between space-y-1">
                    ${renderGmTraitItem(baggage, 'Baggage')}
                    ${renderGmTraitItem(fact1, 'Fact 1')}
                    ${renderGmTraitItem(fact2, 'Fact 2')}
                </div>
            </div>
        `;
        grid.appendChild(card);
    }
}

let ws;
function initWebSocket() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    ws = new WebSocket(`${protocol}//${window.location.host}/ws/game/${roomCode}/${encodeURIComponent(currentUsername)}`);

    ws.onopen = () => {
        const readyRequest = {
            type: "READY_FOR_GAME_START"
        };
        ws.send(JSON.stringify(readyRequest));
    };

    ws.onmessage = (event) => {
        const data = JSON.parse(event.data);
        if (data.type === "GAME_STATE") {
            if (data.gameState) {
                gameState = data.gameState;
                sessionStorage.setItem('gameState', JSON.stringify(gameState));
                renderGmState(gameState);
            }
        }
    };

    ws.onclose = () => setTimeout(initWebSocket, 2000);
}

function setPlayerStatus(isAlive) {
    const select = document.getElementById('selectStatusPlayer');
    if (!select) return;
    const nickname = select.value;
    if (!nickname) {
        alert('Choose a player from the list');
        return;
    }
    const message = {
        type: "UPDATE_PLAYER_IS_ALIVE",
        nickname: nickname,
        isAlive: Boolean(isAlive)
    };

    ws.send(JSON.stringify(message));
}

function startVoting() {
    const message = {
        type: "START_VOTING"
    };
    ws.send(JSON.stringify(message));
}

function endVoting() {
    const message = {
        type: "END_VOTING"
    };
    ws.send(JSON.stringify(message));
}

function mockRevealTrait() {
    const playerSelect = document.getElementById('selectPlayer');
    const traitSelect = document.getElementById('selectTrait');

    if (!playerSelect || !traitSelect) return;

    const nickname = playerSelect.value;
    const rawTrait = traitSelect.value;

    if (!nickname) {
        alert('Choose player');
        return;
    }

    if (!rawTrait) {
        alert('Choose trait');
        return;
    }

    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.warn('WebSocket does not run');
        return;
    }

    const message = {
        type: "UPDATE_PLAYER_TRAIT_VISIBILITY",
        nickname: nickname,
        traitName: rawTrait.toUpperCase(),
        isOpen: true
    };

    ws.send(JSON.stringify(message));
}

function setRandomTraitForPlayer() {
    const playerSelect = document.getElementById('selectRandomTraitPlayer');
    const traitSelect = document.getElementById('selectRandomTraitName');
    if (!playerSelect || !traitSelect) return;

    const nickname = playerSelect.value;
    const rawTrait = traitSelect.value;

    if (!nickname) {
        alert('Choose player');
        return;
    }
    if (!rawTrait) {
        alert('Choose trait');
        return;
    }
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.warn('WebSocket does not run');
        return;
    }

    const message = {
        type: "SET_RANDOM_TRAIT_FOR_PLAYER",
        nickname: nickname,
        traitName: rawTrait.toUpperCase()
    };

    ws.send(JSON.stringify(message));
}

function setStringAsTraitValueForPlayer() {
    const playerSelect = document.getElementById('selectCustomTraitPlayer');
    const traitSelect = document.getElementById('selectCustomTraitName');
    const valueInput = document.getElementById('customTraitValueInput');
    if (!playerSelect || !traitSelect || !valueInput) return;

    const nickname = playerSelect.value;
    const rawTrait = traitSelect.value;
    const value = valueInput.value.trim();

    if (!nickname) {
        alert('Choose player');
        return;
    }
    if (!rawTrait) {
        alert('Choose trait');
        return;
    }
    if (!value) {
        alert('Enter trait value');
        return;
    }
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.warn('WebSocket does not run');
        return;
    }

    const message = {
        type: "SET_STRING_AS_TRAIT_VALUE_FOR_PLAYER",
        nickname: nickname,
        traitName: rawTrait.toUpperCase(),
        value: value
    };

    ws.send(JSON.stringify(message));
    valueInput.value = '';
}

initWebSocket();