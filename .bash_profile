export CLICOLOR=1 
export LSCOLORS=GxFxCxDxBxegedabagaced
alias ls='ls -GFh'
alias ll='ls -l'


function prompt {
  local BLACK="\[\033[0;30m\]"
  local BLACKBOLD="\[\033[1;30m\]"
  local RED="\[\033[0;31m\]"
  local REDBOLD="\[\033[1;31m\]"
  local GREEN="\[\033[0;32m\]"
  local GREENBOLD="\[\033[1;32m\]"
  local YELLOW="\[\033[0;33m\]"
  local YELLOWBOLD="\[\033[1;33m\]"
  local BLUE="\[\033[0;34m\]"
  local BLUEBOLD="\[\033[1;34m\]"
  local PURPLE="\[\033[0;35m\]"
  local PURPLEBOLD="\[\033[1;35m\]"
  local CYAN="\[\033[0;36m\]"
  local CYANBOLD="\[\033[1;36m\]"
  local WHITE="\[\033[0;37m\]"
  local WHITEBOLD="\[\033[1;37m\]"
  local RESETCOLOR="\[\e[00m\]"

  # Git branch in prompt.
  parse_git_branch() {
      git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
  }

  export PS1="\n$RED\u $PURPLE@ $GREEN\w $RESETCOLOR$GREENBOLD\$(parse_git_branch)\n $BLUE[\#] → $RESETCOLOR"
  export PS2=" | → $RESETCOLOR"
}

prompt

export PATH

#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK!!!
#export SDKMAN_DIR="/Users/arvins-macpro/.sdkman"
#[[ -s "/Users/arvins-macpro/.sdkman/bin/sdkman-init.sh" ]] && source "/Users/arvins-macpro/.sdkman/bin/sdkman-init.sh"




export GOPATH=$HOME/go-workspace # don't forget to change your path correctly!
export GOROOT=/usr/local/go
export PATH=$PATH:$GOPATH/bin
export PATH=$PATH:$GOROOT/bin

# bash completion v2
[[ -r "/usr/local/etc/profile.d/bash_completion.sh" ]] && . "/usr/local/etc/profile.d/bash_completion.sh"
source <(kubectl completion bash)
alias k=kubectl
complete -o default -F __start_kubectl k

export PATH="${KREW_ROOT:-$HOME/.krew}/bin:$PATH"

export CONFLUENT_HOME=$HOME/confluent-7.5.2
export PATH=$PATH:$CONFLUENT_HOME/bin

#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK!!!
export SDKMAN_DIR="/Users/arvins-mac/.sdkman"
[[ -s "/Users/arvins-mac/.sdkman/bin/sdkman-init.sh" ]] && source "/Users/arvins-mac/.sdkman/bin/sdkman-init.sh"

