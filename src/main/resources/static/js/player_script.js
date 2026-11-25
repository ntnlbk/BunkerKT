const roomCode = sessionStorage.getItem('roomCode');
const currentUsername = sessionStorage.getItem('playerName');

document.getElementById('headerRoomCode').innerText = roomCode;
document.getElementById('headerPlayerName').innerText = currentUsername;

let gameState = null;
let selectedVoteTargetNickname = null;

function isVotingStage(state) {
    return state && state.stage === 'VOTING';
}

function getPlayerSlotNumber(state, nickname) {
    if (!state || !state.players) return null;
    const idx = state.players.findIndex(p => p.nickname === nickname);
    return idx >= 0 ? idx + 1 : null;
}

function findWhoCurrentUserVotedFor(state) {
    if (!state || !state.players || !currentUsername) return null;

    const me = state.players.find(p => p.nickname === currentUsername);
    if (me && typeof me.votesAgainst === 'number' && me.votesAgainst >= 1) {
        const targetBySlot = state.players[me.votesAgainst - 1];
        if (targetBySlot) return targetBySlot.nickname;
    }

    const mySlot = getPlayerSlotNumber(state, currentUsername);
    if (!mySlot) return null;

    const fallbackTarget = state.players.find(
        p => Array.isArray(p.votesFromPlayers) && p.votesFromPlayers.includes(mySlot)
    );
    return fallbackTarget ? fallbackTarget.nickname : null;
}

function toggleSpecialCardsModal() {
    const modal = document.getElementById('specialCardsModal');
    modal.classList.toggle('hidden');
    if (!modal.classList.contains('hidden')) {
        updateSpecialCardsContent();
    }
}

function toggleVoteModal() {
    if (!isVotingStage(gameState)) return;

    const modal = document.getElementById('voteModal');
    const willOpen = modal.classList.contains('hidden');

    if (willOpen) {
        selectedVoteTargetNickname = findWhoCurrentUserVotedFor(gameState);
        renderVoteModal();
        modal.classList.remove('hidden');
    } else {
        modal.classList.add('hidden');
    }
}

function selectVoteCandidate(nickname) {
    selectedVoteTargetNickname = nickname;
    renderVoteModal();
}

function renderVoteModal() {
    const listEl = document.getElementById('voteCandidatesList');
    const confirmBtn = document.getElementById('confirmVoteBtn');
    if (!listEl || !confirmBtn || !gameState) return;

    const players = gameState.players || [];
    const candidates = players
        .map((player, index) => ({player, slotNumber: index + 1}))
        .filter(({player}) => player && player.isAlive);

    if (candidates.length === 0) {
        listEl.innerHTML = `<div class="text-slate-400 text-center py-4">No eligible survivors to vote for.</div>`;
        confirmBtn.disabled = true;
        return;
    }

    listEl.innerHTML = candidates.map(({player, slotNumber}) => {
        const isSelected = selectedVoteTargetNickname === player.nickname;
        const voters = Array.isArray(player.votesFromPlayers) ? player.votesFromPlayers : [];
        const voteCount = voters.length;

        const voterCircles = voters.length > 0
            ? voters.map(voterSlot => `
                    <span class="inline-flex items-center justify-center w-5 h-5 rounded-full text-[10px] font-bold bg-rose-500/20 text-rose-300 border border-rose-500/60">
                        ${voterSlot}
                    </span>
                `).join('')
            : `<span class="text-[10px] text-slate-500 italic">No votes yet</span>`;

        return `
                <button type="button"
                        onclick="selectVoteCandidate('${player.nickname.replace(/'/g, "\\'")}')"
                        class="w-full text-left p-2.5 rounded-lg border transition-all flex items-center justify-between gap-2 ${
            isSelected
                ? 'bg-rose-500/20 border-rose-400 text-white shadow-[0_0_12px_rgba(244,63,94,0.2)]'
                : 'bg-slate-950/90 border-slate-800 hover:border-rose-500/50 text-slate-200'
        }">
                    <div class="min-w-0">
                        <div class="flex items-center space-x-2">
                            <span class="text-[10px] font-bold px-1.5 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">#${slotNumber}</span>
                            <span class="font-bold text-sm truncate">${player.nickname}</span>
                        </div>
                        <div class="flex items-center flex-wrap gap-1 mt-1.5">
                            ${voterCircles}
                        </div>
                    </div>
                    <div class="text-right shrink-0">
                        <span class="block text-xs font-bold text-rose-400">${voteCount} ${voteCount === 1 ? 'vote' : 'votes'}</span>
                        <span class="text-[10px] uppercase ${isSelected ? 'text-rose-300 font-bold' : 'text-slate-500'}">
                            ${isSelected ? 'SELECTED' : 'SELECT'}
                        </span>
                    </div>
                </button>
            `;
    }).join('');

    confirmBtn.disabled = !selectedVoteTargetNickname;
}

function submitSelectedVote() {
    if (!selectedVoteTargetNickname) return;

    const voteRequest = {
        type: "VOTE",
        nickname: currentUsername,
        targetNickname: selectedVoteTargetNickname
    };
    ws.send(JSON.stringify(voteRequest));
    toggleVoteModal();
}

function updateSpecialCardsContent() {
    if (!gameState || !gameState.players) return;

    const me = gameState.players.find(p => p.nickname === currentUsername);

    if (me && me.specialCards) {
        const card1 = me.specialCards[0] ? me.specialCards[0] : "No card available";
        const card2 = me.specialCards[1] ? me.specialCards[1] : "No card available";

        const el1 = document.getElementById('specialCard1Text');
        const el2 = document.getElementById('specialCard2Text');

        if (el1) el1.innerText = card1;
        if (el2) el2.innerText = card2;
    } else {
        const el1 = document.getElementById('specialCard1Text');
        const el2 = document.getElementById('specialCard2Text');

        if (el1) el1.innerText = "Card data unavailable";
        if (el2) el2.innerText = "Card data unavailable";
    }
}

function renderStageChrome(state) {
    const voting = isVotingStage(state);
    const stageBadge = document.getElementById('stageBadge');
    const voteHeaderBtn = document.getElementById('voteHeaderBtn');
    const votingBanner = document.getElementById('votingBanner');
    const myVoteLabel = document.getElementById('myCurrentVoteLabel');

    if (stageBadge) {
        if (voting) {
            stageBadge.textContent = 'VOTING';
            stageBadge.className = 'text-[10px] px-2 py-0.5 rounded border font-bold tracking-wider bg-rose-500/20 text-rose-300 border-rose-500/60 animate-pulse';
        } else {
            stageBadge.textContent = 'MAIN PHASE';
            stageBadge.className = 'text-[10px] px-2 py-0.5 rounded border font-bold tracking-wider bg-emerald-500/10 text-emerald-400 border-emerald-500/30';
        }
    }

    if (voteHeaderBtn) {
        voteHeaderBtn.classList.toggle('hidden', !voting);
    }

    if (votingBanner) {
        votingBanner.classList.toggle('hidden', !voting);
    }

    if (voting && myVoteLabel) {
        const currentTarget = findWhoCurrentUserVotedFor(state);
        myVoteLabel.textContent = currentTarget
            ? `YOUR VOTE: ${currentTarget.toUpperCase()}`
            : 'YOUR VOTE: NONE';
    }
}

function renderVotesBadgeHtml(player, voting) {
    const voters = Array.isArray(player?.votesFromPlayers) ? player.votesFromPlayers : [];
    const voteCount = voters.length;

    if (!voting && voteCount === 0) {
        return '';
    }

    const circlesHtml = voters.map(voterSlot => {
        const voterPlayer = gameState?.players?.[voterSlot - 1];
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
                    ${voteCount}V
                </span>
            </div>
        `;
}

function renderGameState(state) {
    if (!state) return;

    renderStageChrome(state);

    if (state.catastropheDescription) {
        document.querySelector('.grid.grid-cols-1.sm\\:grid-cols-3 > div:nth-child(1) p').innerText = state.catastropheDescription;
    }
    if (state.bunkerDescription) {
        document.querySelector('.grid.grid-cols-1.sm\\:grid-cols-3 > div:nth-child(2) p').innerText = state.bunkerDescription;
    }
    if (state.bunkerInventory) {
        document.querySelector('.grid.grid-cols-1.sm\\:grid-cols-3 > div:nth-child(3) p').innerText = state.bunkerInventory;
    }

    if (typeof updateSpecialCardsContent === 'function') {
        updateSpecialCardsContent();
    }

    const grid = document.getElementById('playersGrid');
    if (!grid) return;
    grid.innerHTML = '';

    const players = state.players || [];
    const voting = isVotingStage(state);
    const myVotedTarget = findWhoCurrentUserVotedFor(state);

    for (let i = 0; i < 12; i++) {
        const slotNumber = i + 1;
        const player = players[i] || null;
        const isOccupied = !!player;
        const nickname = player ? player.nickname : `SLOT #${slotNumber}`;
        const isMe = player && player.nickname === currentUsername;
        const isAlive = player ? player.isAlive : true;
        const isVotedByMe = isOccupied && myVotedTarget === player.nickname;

        const renderTraitItem = (traitObj, fallbackName) => {
            if (!isOccupied || !traitObj) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">${fallbackName}</div>`;
            }

            if (!isMe && !traitObj.isOpen) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">???</div>`;
            }

            if (isMe) {
                const isOpenToAll = traitObj.isOpen;
                const statusBadge = isOpenToAll
                    ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-1" title="Видно всем"><i class="fa-solid fa-eye"></i></span>`
                    : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-1" title="Видно только вам"><i class="fa-solid fa-lock"></i></span>`;

                return `
                    <div class="flex items-start justify-between text-white text-xs py-1" title="${traitObj.value}">
                        <span class="break-words leading-tight">${traitObj.value}</span>
                        ${statusBadge}
                    </div>
                `;
            }

            return `<div class="text-white font-medium text-xs py-1 break-words leading-tight" title="${traitObj.value}">${traitObj.value}</div>`;
        };

        const renderHealthWithScore = (healthObj, scoreObj) => {
            if (!isOccupied) {
                return `<div class="text-slate-600 italic text-xs py-1 truncate">Health</div>`;
            }

            const canShowHealth = Boolean(healthObj) && (isMe || Boolean(healthObj.isOpen));
            const canShowScore = Boolean(scoreObj) && scoreObj.value !== undefined && scoreObj.value !== null && String(scoreObj.value) !== '' && (isMe || Boolean(scoreObj.isOpen));

            const healthTextHtml = canShowHealth
                ? `<span class="text-white ${isMe ? '' : 'font-medium'} break-words leading-tight" title="${healthObj.value}">${healthObj.value}</span>`
                : `<span class="text-slate-600 italic leading-tight">???</span>`;

            const healthEyeLockHtml = (isMe && healthObj)
                ? (healthObj.isOpen
                    ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-1" title="Здоровье видно всем"><i class="fa-solid fa-eye"></i></span>`
                    : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-1" title="Здоровье видно только вам"><i class="fa-solid fa-lock"></i></span>`)
                : '';

            const scoreText = canShowScore ? `${scoreObj.value}%` : '';
            const scoreBadgeHtml = canShowScore
                ? `<span class="text-[11px] font-bold text-rose-400 bg-rose-950/60 border border-rose-500/40 px-1.5 py-0.2 rounded shrink-0 ml-1" title="Disease severity: ${scoreText}">${scoreText}</span>`
                : '';

            const scoreEyeLockHtml = (isMe && canShowScore)
                ? (scoreObj.isOpen
                    ? `<span class="text-[9px] text-emerald-400 font-normal shrink-0 ml-0.5" title="Health score виден всем"><i class="fa-solid fa-eye"></i></span>`
                    : `<span class="text-[9px] text-amber-500 font-normal shrink-0 ml-0.5" title="Health score виден только вам"><i class="fa-solid fa-lock"></i></span>`)
                : '';

            const combinedTitle = [
                canShowHealth ? healthObj.value : null,
                canShowScore ? `Health score: ${scoreText}` : null
            ].filter(Boolean).join(' — ');

            return `
                <div class="flex items-start justify-between gap-1 text-xs py-1" title="${combinedTitle}">
                    <div class="flex items-baseline flex-wrap gap-1 min-w-0">
                        ${healthTextHtml}
                        ${scoreBadgeHtml}
                    </div>
                    <div class="flex items-center shrink-0">
                        ${healthEyeLockHtml}
                        ${scoreEyeLockHtml}
                    </div>
                </div>
            `;
        };

        const getHeaderTraitValue = (traitObj, fallback) => {
            if (!isOccupied || !traitObj) return fallback;
            if (!isMe && !traitObj.isOpen) return `???`;
            return traitObj.value;
        };

        const renderHeaderBiologyBadge = (bioObj) => {
            if (!isOccupied || !bioObj) {
                return `<span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800"><i class="fa-solid fa-dna mr-1 text-emerald-500"></i>Biology</span>`;
            }
            if (!isMe && !bioObj.isOpen) {
                return `<span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800"><i class="fa-solid fa-dna mr-1 text-emerald-500"></i>???</span>`;
            }
            const statusBadge = isMe
                ? (bioObj.isOpen
                    ? `<i class="fa-solid fa-eye text-[9px] text-emerald-400 ml-1" title="Видно всем"></i>`
                    : `<i class="fa-solid fa-lock text-[9px] text-amber-500 ml-1" title="Видно только вам"></i>`)
                : '';

            return `<span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800 inline-flex items-center" title="${bioObj.value}"><i class="fa-solid fa-dna mr-1 text-emerald-500"></i>${bioObj.value}${statusBadge}</span>`;
        };

        const sexVal = getHeaderTraitValue(player ? player.sex : null, 'Gender');
        const ageVal = getHeaderTraitValue(player ? player.age : null, 'Age');
        const bioHtml = renderHeaderBiologyBadge(player ? player.biology : null);

        const card = document.createElement('div');
        card.className = `relative overflow-hidden bg-slate-900/95 border rounded p-2.5 flex flex-col justify-between transition-all ${
            isOccupied
                ? (isAlive
                    ? (isVotedByMe
                        ? 'border-rose-400 ring-2 ring-rose-500/50 shadow-[0_0_18px_rgba(244,63,94,0.25)]'
                        : (isMe ? 'border-amber-500 shadow-[0_0_15px_rgba(245,158,11,0.2)]' : 'border-emerald-500/70'))
                    : 'border-rose-900/60 opacity-60 bg-rose-950/20')
                : 'border-slate-800/80 opacity-40'
        }`;

        const exiledStampHtml = (!isAlive && isOccupied) ? `
            <div class="absolute inset-0 flex items-center justify-center z-10 pointer-events-none">
                <div class="border-4 border-rose-500/80 text-rose-500 font-black text-xl sm:text-2xl px-3 py-1 uppercase tracking-widest rotate-[-12deg] shadow-[0_0_15px_rgba(244,63,94,0.3)] bg-slate-950/80 backdrop-blur-[1px]">
                    EXILED
                </div>
            </div>
        ` : '';

        const traits = player ? player : {};
        const votesHtml = isOccupied ? renderVotesBadgeHtml(player, voting) : '';

        card.innerHTML = `
            ${exiledStampHtml}
            <div class="border-b border-slate-800 pb-1.5 mb-1.5 shrink-0">
                <div class="flex items-center justify-between gap-1">
                    <div class="flex items-center space-x-1.5 min-w-0">
                        <span class="text-[10px] font-bold font-mono px-1.5 py-0.5 rounded border shrink-0 ${
            isMe
                ? 'bg-amber-500/20 text-amber-400 border-amber-500/40'
                : (isOccupied ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/40' : 'bg-slate-800 text-slate-500 border-slate-700')
        }">
                            ${slotNumber}
                        </span>
                        <span class="text-xs sm:text-sm font-bold ${isOccupied ? (isAlive ? (isMe ? 'text-amber-400' : 'text-white') : 'text-rose-400 line-through') : 'text-slate-600'} truncate">
                            ${nickname} ${isMe ? '(YOU)' : ''}
                        </span>
                    </div>
                    <div class="flex items-center space-x-1 shrink-0">
                        ${isVotedByMe ? `<span class="text-[9px] px-1.5 py-0.5 rounded uppercase font-bold bg-rose-500 text-slate-950">Your Vote</span>` : ''}
                        <span class="text-[9px] px-1.5 py-0.5 rounded uppercase font-semibold ${
            isOccupied
                ? (isAlive ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30')
                : 'text-slate-600'
        }">
                            ${isOccupied ? (isAlive ? 'Alive' : 'Dead') : 'Empty'}
                        </span>
                    </div>
                </div>
                ${isOccupied ? `
                <div class="flex items-center flex-wrap gap-1.5 mt-1 text-[11px] text-slate-300 font-mono">
                    <span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800"><i class="fa-solid fa-venus-mars mr-1 text-emerald-500"></i>${sexVal}</span>
                    <span class="bg-slate-950 px-1.5 py-0.5 rounded border border-slate-800"><i class="fa-solid fa-cake-candles mr-1 text-emerald-500"></i>${ageVal} yrs</span>
                    ${bioHtml}
                </div>
                ${votesHtml}` : ''}
            </div>

            <div class="grid grid-cols-2 gap-2 text-xs flex-1">
                <div class="flex flex-col bg-slate-950/80 p-2 rounded border border-slate-800 justify-between space-y-1">
                    ${renderTraitItem(traits.profession, 'Profession')}
                    ${renderHealthWithScore(traits.health, traits.healthScore)}
                    ${renderTraitItem(traits.hobby, 'Hobby')}
                    ${renderTraitItem(traits.phobia, 'Phobia')}
                </div>

                <div class="flex flex-col bg-slate-950/80 p-2 rounded border border-slate-800 justify-between space-y-1">
                    ${renderTraitItem(traits.baggage, 'Baggage')}
                    ${renderTraitItem(traits.factOne, 'Fact 1')}
                    ${renderTraitItem(traits.factTwo, 'Fact 2')}
                </div>
            </div>
        `;
        grid.appendChild(card);
    }

    if (!document.getElementById('voteModal').classList.contains('hidden')) {
        if (!voting) {
            document.getElementById('voteModal').classList.add('hidden');
        } else {
            renderVoteModal();
        }
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
                renderGameState(gameState);
            }
        }
    };

    ws.onclose = () => setTimeout(initWebSocket, 2000);
}

(function startWebSocketConnection() {
    initWebSocket();
})();